package com.ffrontera._generales.backend.modules.catalog.repository;

import com.ffrontera._generales.backend.modules.catalog.domain.Brand;
import com.ffrontera._generales.backend.modules.catalog.dto.BrandDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Long id);

    @Query("SELECT b FROM Brand b WHERE " +
            "(:name IS NULL OR :name = '' OR LOWER(b.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:active IS NULL OR b.active = :active)")
    Page<Brand> search(String name, Boolean active, Pageable pageable);
}
