package com.ffrontera._generales.backend.modules.suppliers.controller;

import com.ffrontera._generales.backend.modules.catalog.service.PriceUpdateService;
import com.ffrontera._generales.backend.modules.suppliers.dto.SupplierDTO;
import com.ffrontera._generales.backend.modules.suppliers.service.SupplierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class SupplierController {

    private final SupplierService supplierService;
    private final PriceUpdateService priceUpdateService;

    @PostMapping
    public ResponseEntity<SupplierDTO> createSupplier(@RequestBody @Valid SupplierDTO supplierDTO) {
        return new ResponseEntity<>(supplierService.createSupplier(supplierDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<SupplierDTO>> getSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Boolean active) {
        return ResponseEntity.ok(supplierService.getSuppliers(page, size, sortBy, sortDir, query, active));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDTO> updateSupplier(@PathVariable Long id, @RequestBody @Valid SupplierDTO supplierDTO) {
        return ResponseEntity.ok(supplierService.updateSupplier(id, supplierDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{id}/price-list", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadPriceList(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String result = priceUpdateService.processPriceList(id, file);
            return ResponseEntity.ok(Map.of("message", result));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Error processing file: " + e.getMessage()));
        }
    }
}
