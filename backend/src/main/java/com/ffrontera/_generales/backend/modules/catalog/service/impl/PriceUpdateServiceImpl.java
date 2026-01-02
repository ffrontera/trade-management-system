package com.ffrontera._generales.backend.modules.catalog.service.impl;

import com.ffrontera._generales.backend.common.exception.ResourceNotFoundException;
import com.ffrontera._generales.backend.modules.catalog.domain.Product;
import com.ffrontera._generales.backend.modules.catalog.domain.SupplierProduct;
import com.ffrontera._generales.backend.modules.catalog.dto.PriceListRowDTO;
import com.ffrontera._generales.backend.modules.catalog.repository.ProductRepository;
import com.ffrontera._generales.backend.modules.catalog.repository.SupplierProductRepository;
import com.ffrontera._generales.backend.modules.catalog.service.ExcelReaderService;
import com.ffrontera._generales.backend.modules.catalog.service.PriceUpdateService;
import com.ffrontera._generales.backend.modules.suppliers.domain.Supplier;
import com.ffrontera._generales.backend.modules.suppliers.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceUpdateServiceImpl implements PriceUpdateService {

    @Value("${app.price-list.validity-days:30}")
    private int priceValidityDays;

    private final SupplierRepository supplierRepository;
    private final SupplierProductRepository supplierProductRepository;
    private final ProductRepository productRepository;
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
                            supplierProduct -> {
                                if (supplierProduct.getCostPrice().compareTo(row.newCost()) != 0) {
                                    supplierProduct.setCostPrice(row.newCost());
                                    supplierProduct.setSupplierProductName(row.description());
                                    supplierProductRepository.save(supplierProduct);

                                    updateMainProductCost(supplierProduct.getProduct());

                                    updatedCount.getAndIncrement();
                                } else {
                                    supplierProduct.setLastUpdated(LocalDateTime.now());
                                    supplierProductRepository.save(supplierProduct);
                                    skippedCount.incrementAndGet();
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

    private void updateMainProductCost(Product product) {

        List<SupplierProduct> allSupplierOptions = supplierProductRepository.findByProductId(product.getId());
        LocalDateTime validityThreshold = LocalDateTime.now().minusDays(priceValidityDays);

        allSupplierOptions.stream()
                .filter(sp -> sp.getCostPrice().compareTo(BigDecimal.ZERO) > 0)
                .filter(sp -> sp.getLastUpdated() != null && sp.getLastUpdated().isAfter(validityThreshold))
                .map(SupplierProduct::getCostPrice)
                .min(Comparator.naturalOrder())
                .ifPresentOrElse(minCost -> {
                    if (product.getCostPrice().compareTo(minCost) != 0) {
                        log.info("Actualizando costo producto ID {} de {} a {} (Fuente validada)", product.getId(), product.getCostPrice(), minCost);
                        product.setCostPrice(minCost);
                        productRepository.save(product);
                    }
                },
                        () -> {
                            log.warn("Producto ID {} no tiene precios de proveedores vigentes (actualizados hace menos de {} días). No se actualiza el costo.",
                                    product.getId(), priceValidityDays);
                });
    }
}