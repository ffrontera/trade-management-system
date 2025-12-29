package com.ffrontera._generales.backend.modules.catalog.repository;

import com.ffrontera._generales.backend.modules.catalog.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("SELECT c FROM Category c WHERE " +
            "(:name IS NULL OR :name = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:active IS NULL OR c.active = :active)")
    Page<Category> search(String name, Boolean active, Pageable pageable);

    boolean existsByName(String name);
}
