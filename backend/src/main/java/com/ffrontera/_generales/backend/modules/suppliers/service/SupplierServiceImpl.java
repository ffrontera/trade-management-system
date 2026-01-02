package com.ffrontera._generales.backend.modules.suppliers.service;

import com.ffrontera._generales.backend.common.exception.DuplicateResourceException;
import com.ffrontera._generales.backend.common.exception.ResourceNotFoundException;
import com.ffrontera._generales.backend.modules.suppliers.dto.SupplierDTO;
import com.ffrontera._generales.backend.modules.suppliers.mapper.SupplierMapper;
import com.ffrontera._generales.backend.modules.suppliers.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    @Transactional
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        if (supplierRepository.existsByCode(supplierDTO.code()))
            throw new DuplicateResourceException("Proveedor con código '" + supplierDTO.code() + "' ya existe.");
        if (supplierDTO.email() != null && supplierRepository.existsByEmail(supplierDTO.email()))
            throw new DuplicateResourceException("Proveedor con email '" + supplierDTO.email() + "' ya existe.");

        var supplier = supplierMapper.toEntity(supplierDTO);
        supplier.setActive(true);
        return supplierMapper.toDTO(supplierRepository.save(supplier));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SupplierDTO> getSuppliers(int page, int size, String sortBy, String sortDir, String query, Boolean active) {
        var sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        var pageable = PageRequest.of(page, size, sort);

        return supplierRepository.search(query, active, pageable)
                .map(supplierMapper::toDTO);
    }

    @Override
    @Transactional
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        var supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con ID '" + id + "' no encontrado."));

        if (!Objects.equals(supplier.getCode(), supplierDTO.code()) && supplierRepository.existsByCode(supplierDTO.code()))
            throw new DuplicateResourceException("Proveedor con código '" + supplierDTO.code() + "' ya existe.");

        if (!Objects.equals(supplier.getEmail(), supplierDTO.email()) && supplierRepository.existsByEmail(supplierDTO.email()))
            throw new DuplicateResourceException("Proveedor con email '" + supplierDTO.email() + "' ya existe.");

        supplier.setName(supplierDTO.name());
        supplier.setCode(supplierDTO.code());
        supplier.setEmail(supplierDTO.email());
        supplier.setPhone(supplierDTO.phone());
        supplier.setAddress(supplierDTO.address());
        if (supplierDTO.active() != null)
            supplier.setActive(supplierDTO.active());

        return supplierMapper.toDTO(supplierRepository.save(supplier));
    }

    @Override
    @Transactional
    public void deleteSupplier(Long id) {
        var supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor con ID '" + id + "' no encontrado."));
        supplier.setActive(false);
        supplierRepository.save(supplier);
    }
}
