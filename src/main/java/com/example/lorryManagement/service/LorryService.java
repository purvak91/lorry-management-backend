package com.example.lorryManagement.service;

import com.example.lorryManagement.entity.LorryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LorryService {
    LorryEntity save(LorryEntity lorryEntity);
    Optional<LorryEntity> findByLr(Long lr);
    void deleteByLr(Long lr);
    LorryEntity update(LorryEntity lorryEntity);
    Long getNextLr();
    Page<LorryEntity> findWithFilters(
            String search,
            LocalDate from,
            LocalDate to,
            Pageable pageable
    );
    List<String> getDistinctLorryNumbers();
    List<String> getDistinctFromLocations();
    List<String> getDistinctToLocations();
    List<String> getDistinctConsignorNames();
}
