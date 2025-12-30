package com.ffrontera._generales.backend.modules.suppliers.service;

import com.ffrontera._generales.backend.modules.suppliers.dto.SupplierDTO;
import org.springframework.data.domain.Page;

public interface SupplierService {

    SupplierDTO createSupplier(SupplierDTO supplierDTO);
    Page<SupplierDTO> getSuppliers(int page, int size, String sortBy, String sortDir, String query, Boolean active);
    SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO);
    void deleteSupplier(Long id);

}
