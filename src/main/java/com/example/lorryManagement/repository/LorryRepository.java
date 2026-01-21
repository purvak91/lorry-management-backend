package com.example.lorryManagement.repository;

import com.example.lorryManagement.entity.LorryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LorryRepository
        extends JpaRepository<LorryEntity, Long>,
        JpaSpecificationExecutor<LorryEntity> {
    Page<LorryEntity> findByDate(LocalDate date, Pageable pageable);
    Page<LorryEntity> findByLorryNumber(String lorryNumber, Pageable pageable);
    Page<LorryEntity> findByConsignorName(String consignorName, Pageable pageable);
    Page<LorryEntity> findAllByDateBetween(LocalDate start, LocalDate end, Pageable pageable);

    @Query("SELECT COALESCE(MAX(l.lr), 0) FROM  lorry l")
    Long findMaxLr();

    @Query("""
        select distinct l.lorryNumber
        from lorry l
        where l.lorryNumber is not null
        order by l.lorryNumber
    """)
    List<String> findDistinctLorryNumbers();

    @Query("""
        select distinct l.fromLocation
        from lorry l
        where l.fromLocation is not null
        order by l.fromLocation
    """)
    List<String> findDistinctFromLocations();

    @Query("""
        select distinct l.toLocation
        from lorry l
        where l.toLocation is not null
        order by l.toLocation
    """)
    List<String> findDistinctToLocations();

    @Query("""
        select distinct l.consignorName
        from lorry l
        where l.consignorName is not null
        order by l.consignorName
    """)
    List<String> findDistinctConsignorNames();
}
