package com.ffrontera._generales.backend.modules.suppliers.mapper;

import com.ffrontera._generales.backend.modules.suppliers.domain.Supplier;
import com.ffrontera._generales.backend.modules.suppliers.dto.SupplierDTO;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierDTO toDTO(Supplier supplier) {
        if (supplier == null) {
            return null;
        }
        return new SupplierDTO(
                supplier.getId(),
                supplier.getName(),
                supplier.getCode(),
                supplier.getEmail(),
                supplier.getPhone(),
                supplier.getAddress(),
                supplier.getActive()
        );
    }

    public Supplier toEntity(SupplierDTO supplierDTO) {
        if (supplierDTO == null) {
            return null;
        }
        Supplier supplier = new Supplier();
        supplier.setId(supplierDTO.id());
        supplier.setName(supplierDTO.name());
        supplier.setCode(supplierDTO.code());
        supplier.setEmail(supplierDTO.email());
        supplier.setPhone(supplierDTO.phone());
        supplier.setAddress(supplierDTO.address());
        supplier.setActive(supplierDTO.active() != null ? supplierDTO.active() : true);
        return supplier;
    }
}
