package com.ffrontera._generales.backend.modules.catalog.service;

import com.ffrontera._generales.backend.modules.catalog.domain.Product;
import com.ffrontera._generales.backend.modules.catalog.domain.SupplierProduct;
import com.ffrontera._generales.backend.modules.catalog.dto.PriceListRowDTO;
import com.ffrontera._generales.backend.modules.catalog.repository.ProductRepository;
import com.ffrontera._generales.backend.modules.catalog.repository.SupplierProductRepository;
import com.ffrontera._generales.backend.modules.catalog.service.impl.PriceUpdateServiceImpl;
import com.ffrontera._generales.backend.modules.suppliers.domain.Supplier;
import com.ffrontera._generales.backend.modules.suppliers.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceUpdateServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierProductRepository supplierProductRepository;

    @Mock
    private ProductRepository productRepository;;

    @Mock
    private ExcelReaderService excelReaderService;;

    @InjectMocks
    private PriceUpdateServiceImpl service;

    @Test
    void processPriceList_ShouldUpdatePrice_WhenLinkExists() throws Exception {
        Long supplierId = 1L;
        String supplierSku = "SUP-001";
        BigDecimal oldPrice = new BigDecimal("100");
        BigDecimal newPrice = new BigDecimal("120");
        Product product = new Product();
        product.setId(10L);

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(new Supplier()));

        List<PriceListRowDTO> fakeRows = List.of(
                new PriceListRowDTO(supplierSku, "Desc", newPrice)
        );
        when(excelReaderService.parsePriceList(any())).thenReturn(fakeRows);

        SupplierProduct existingLink = new SupplierProduct();
        existingLink.setSupplierSku(supplierSku);
        existingLink.setCostPrice(oldPrice);
        existingLink.setProduct(product);

        when(supplierProductRepository.findBySupplierIdAndSupplierSku(supplierId, supplierSku))
                .thenReturn(Optional.of(existingLink));

        service.processPriceList(supplierId, mock(MultipartFile.class));

        verify(supplierProductRepository).save(argThat(sp ->
                sp.getCostPrice().equals(newPrice)
        ));
    }

    @Test
    void processPriceList_ShouldIgnore_WhenLinkDoesNotExist() throws Exception {
        Long supplierId = 1L;

        when(supplierRepository.findById(supplierId)).thenReturn(Optional.of(new Supplier()));

        List<PriceListRowDTO> fakeRows = List.of(
                new PriceListRowDTO("UNKNOWN-SKU", "Desc", BigDecimal.TEN)
        );
        when(excelReaderService.parsePriceList(any())).thenReturn(fakeRows);

        when(supplierProductRepository.findBySupplierIdAndSupplierSku(supplierId, "UNKNOWN-SKU"))
                .thenReturn(Optional.empty());

        service.processPriceList(supplierId, mock(MultipartFile.class));

        verify(supplierProductRepository, never()).save(any());
        verify(productRepository, never()).save(any());
    }

}