package com.example.lorryManagement.repository;

import com.example.lorryManagement.entity.LorryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LorryRepository extends JpaRepository<LorryEntity, Long> {
    public List<LorryEntity> findByDate(LocalDate date);
    public List<LorryEntity> findByLorryNumber(String lorryNumber);
    public List<LorryEntity> findByConsignorName(String consignorName);
    public List<LorryEntity> findAllByDateBetween(LocalDate start, LocalDate end);

}
