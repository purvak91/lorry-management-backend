package com.example.lorryManagement.repository;

import com.example.lorryManagement.entity.LorryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface LorryRepository extends JpaRepository<LorryEntity, Long> {
    Page<LorryEntity> findByDate(LocalDate date, Pageable pageable);
    Page<LorryEntity> findByLorryNumber(String lorryNumber, Pageable pageable);
    Page<LorryEntity> findByConsignorName(String consignorName, Pageable pageable);
    Page<LorryEntity> findAllByDateBetween(LocalDate start, LocalDate end, Pageable pageable);

}
