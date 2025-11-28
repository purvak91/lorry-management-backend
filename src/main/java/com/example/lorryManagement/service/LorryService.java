package com.example.lorryManagement.service;

import com.example.lorryManagement.entity.LorryEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LorryService {
    public LorryEntity save(LorryEntity lorryEntity);
    public Optional<LorryEntity> findByLr(Long lr);
    public List<LorryEntity> findAll();
    public void deleteByLr(Long lr);
    public LorryEntity update(LorryEntity lorryEntity);
    public List<LorryEntity> findByDate(LocalDate date);
    public List<LorryEntity> findByLorryNumber(String lorryNumber);
    public List<LorryEntity> findByConsignorName(String consignorName);
    public List<LorryEntity> findByDateRange(LocalDate start, LocalDate end);
}
