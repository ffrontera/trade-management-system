package com.ffrontera._generales.backend.modules.suppliers.service;

import com.ffrontera._generales.backend.common.exception.DuplicateResourceException;
import com.ffrontera._generales.backend.modules.suppliers.domain.Supplier;
import com.ffrontera._generales.backend.modules.suppliers.dto.SupplierDTO;
import com.ffrontera._generales.backend.modules.suppliers.mapper.SupplierMapper;
import com.ffrontera._generales.backend.modules.suppliers.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private SupplierMapper supplierMapper;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    void create_ShouldReturnDto_WhenSupplierIsNew() {

        SupplierDTO inputDto = new SupplierDTO(null, "Provider1", "CODE123", "provider1@mail.com", "1234567890", "street 2134234, City, Povince", null);
        Supplier supplierEntity = new Supplier(1L, "Provider1", "CODE123",  "provider1@mail.com", "1234567890","street 2134234, City, Povince",  true);
        SupplierDTO expectedDto = new SupplierDTO(1L, "Provider1", "CODE123", "provider1@mail.com", "1234567890", "street 2134234, City, Povince", true);

        when(supplierRepository.existsByCode("CODE123")).thenReturn(false);
        when(supplierRepository.existsByEmail("provider1@mail.com")).thenReturn(false);
        when(supplierMapper.toEntity(inputDto)).thenReturn(supplierEntity);
        when(supplierRepository.save(supplierEntity)).thenReturn(supplierEntity);
        when(supplierMapper.toDTO(supplierEntity)).thenReturn(expectedDto);

        SupplierDTO result = supplierService.createSupplier(inputDto);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Provider1", result.name());

        verify(supplierRepository, times(1)).save(supplierEntity);
    }

    @Test
    void create_ShouldThrowException_WhenCodeExists() {

        SupplierDTO inputDto = new SupplierDTO(null, "Provider2", "CODE123", "provider2@mail.com", "0987654321", "street 2134234, City, Povince", null);

        when(supplierRepository.existsByCode("CODE123")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            supplierService.createSupplier(inputDto);
        });

        verify(supplierRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowException_WhenEmailExists() {

        SupplierDTO inputDto = new SupplierDTO(null, "Provider2", "CODE456", "provider1@mail.com", "0987654321", "street 2134234, City, Povince", null);

        when(supplierRepository.existsByEmail("provider1@mail.com")).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> {
            supplierService.createSupplier(inputDto);
        });

        verify(supplierRepository, never()).save(any());
    }

    @Test
    void delete_ShouldSoftDelete_WhenIdExists() {
        Long supplierId = 1L;
        Supplier existingSupplier = new Supplier(supplierId, "Provider1", "CODE123", "provider1@mail.com", "1234567890", "street 2134234, City, Povince", true);

        when(supplierRepository.findById(supplierId)).thenReturn(java.util.Optional.of(existingSupplier));
        when(supplierRepository.save(any(Supplier.class))).thenAnswer(invocation -> invocation.getArgument(0));

        supplierService.deleteSupplier(supplierId);

        assertFalse(existingSupplier.getActive());
        verify(supplierRepository).save(existingSupplier);
    }
}