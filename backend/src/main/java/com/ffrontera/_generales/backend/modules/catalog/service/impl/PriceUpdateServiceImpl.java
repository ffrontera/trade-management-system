package com.ffrontera._generales.backend.modules.catalog.service.impl;

import com.ffrontera._generales.backend.common.exception.ResourceNotFoundException;
import com.ffrontera._generales.backend.modules.catalog.dto.PriceListRowDTO;
import com.ffrontera._generales.backend.modules.catalog.repository.SupplierProductRepository;
import com.ffrontera._generales.backend.modules.catalog.service.ExcelReaderService;
import com.ffrontera._generales.backend.modules.catalog.service.PriceUpdateService;
import com.ffrontera._generales.backend.modules.suppliers.domain.Supplier;
import com.ffrontera._generales.backend.modules.suppliers.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceUpdateServiceImpl implements PriceUpdateService {

    private final SupplierRepository supplierRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final ExcelReaderService excelReaderService;

    @Override
    @Transactional
    public String processPriceList(Long supplierId, MultipartFile file) throws IOException {

        Supplier supplier = supplierRepository.findById(supplierId)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con id '" + supplierId + "' no encontrado."));

        List<PriceListRowDTO> rows = excelReaderService.parsePriceList(file);

        AtomicInteger updatedCount = new AtomicInteger(0);
        AtomicInteger skippedCount = new AtomicInteger(0);

        for (PriceListRowDTO row : rows) {
            supplierProductRepository.findBySupplierIdAndSupplierSku(supplierId, row.supplierSku())
                    .ifPresentOrElse(
                            existingLink -> {
                                if (existingLink.getCostPrice().compareTo(row.newCost()) != 0) {
                                    existingLink.setCostPrice(row.newCost());
                                    existingLink.setSupplierProductName(row.description());
                                    supplierProductRepository.save(existingLink);
                                    updatedCount.getAndIncrement();
                                } else {
                                    skippedCount.getAndIncrement();
                                }
                            },
                            () -> {
                                log.debug("Producto no vinculado: {} - {}", row.supplierSku(), row.description());
                                skippedCount.getAndIncrement();
                            }
                    );
        }
        return String.format("Proceso finalizado. Actualizados: %d, Omitidos: %d",
                updatedCount.get(), skippedCount.get());
    }
}