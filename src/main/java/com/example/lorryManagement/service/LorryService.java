package com.example.lorryManagement.service;

import com.example.lorryManagement.entity.LorryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LorryService {
    Page<LorryEntity> findAll(Pageable pageable);
    LorryEntity save(LorryEntity lorryEntity);
    Optional<LorryEntity> findByLr(Long lr);
    void deleteByLr(Long lr);
    LorryEntity update(LorryEntity lorryEntity);
    Page<LorryEntity> findByDate(LocalDate date, Pageable pageable);
    Page<LorryEntity> findByLorryNumber(String lorryNumber, Pageable pageable);
    Page<LorryEntity> findByConsignorName(String consignorName,  Pageable pageable);
    Page<LorryEntity> findByDateRange(LocalDate start, LocalDate end,  Pageable pageable);

    Long getNextLr();
}
