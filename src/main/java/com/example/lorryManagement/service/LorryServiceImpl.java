package com.example.lorryManagement.service;

import com.example.lorryManagement.entity.LorryEntity;
import com.example.lorryManagement.repository.LorryRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LorryServiceImpl implements LorryService {
    private final LorryRepository lorryRepository;

    public LorryServiceImpl(LorryRepository lorryRepository) {
        this.lorryRepository = lorryRepository;
    }

    @Override
    public LorryEntity save(LorryEntity lorryEntity) {
        if (lorryRepository.existsById(lorryEntity.getLr())) {
            throw new DuplicateKeyException("LR" + lorryEntity.getLr() + " already exists");
        }
        return lorryRepository.save(lorryEntity);
    }

    @Override
    public Optional<LorryEntity> findByLr(Long lr) {
        return lorryRepository.findById(lr);
    }

    @Override
    public Page<LorryEntity> findAll(Pageable pageable) {
        return lorryRepository.findAll(pageable);
    }

    @Override
    public void deleteByLr(Long lr) {
        if (!lorryRepository.existsById(lr)) {
            throw new NoSuchElementException("LR not found: " + lr);
        }
        lorryRepository.deleteById(lr);
    }

    @Override
    @Transactional
    public LorryEntity update(LorryEntity lorryEntity) {
        Long lr = lorryEntity.getLr();
        if (lr == null) {
            throw new IllegalArgumentException("LR must be provided for update");
        }
        Optional<LorryEntity> lorry = lorryRepository.findById(lr);

        if (lorry.isEmpty()) {
            throw new NoSuchElementException("LR not found: " + lr);
        }

        // if everything is well then we move to actually update the code

        LorryEntity existing =  lorry.get();

        if (lorryEntity.getLorryNumber() != null) {
            existing.setLorryNumber(lorryEntity.getLorryNumber());
        }
        if (lorryEntity.getDate() != null) {
            existing.setDate(lorryEntity.getDate());
        }
        if (lorryEntity.getFromLocation() != null) {
            existing.setFromLocation(lorryEntity.getFromLocation());
        }
        if (lorryEntity.getToLocation() != null) {
            existing.setToLocation(lorryEntity.getToLocation());
        }
        if (lorryEntity.getConsignorName() != null) {
            existing.setConsignorName(lorryEntity.getConsignorName());
        }
        if (lorryEntity.getConsignorAddress() != null) {
            existing.setConsignorAddress(lorryEntity.getConsignorAddress());
        }
        if (lorryEntity.getWeight() != null) {
            existing.setWeight(lorryEntity.getWeight());
        }
        if (lorryEntity.getFreight() != null) {
            existing.setFreight(lorryEntity.getFreight());
        }
        if (lorryEntity.getDescription() != null) {
            existing.setDescription(lorryEntity.getDescription());
        }

        return lorryRepository.save(existing);
    }

    @Override
    public Page<LorryEntity> findByDate(LocalDate date, Pageable pageable) {
        return lorryRepository.findByDate(date, pageable);
    }

    @Override
    public Page<LorryEntity> findByLorryNumber(String lorryNumber,Pageable pageable) {
        return lorryRepository.findByLorryNumber(lorryNumber, pageable);
    }

    @Override
    public Page<LorryEntity> findByConsignorName(String consignorName, Pageable pageable) {
        return lorryRepository.findByConsignorName(consignorName, pageable);
    }

    @Override
    public Page<LorryEntity> findByDateRange(LocalDate start, LocalDate end, Pageable pageable) {
        return lorryRepository.findAllByDateBetween(start, end, pageable);
    }

    @Override
    public Long getNextLr() {
        Long maxLr = lorryRepository.findMaxLr();
        return maxLr + 1;
    }
}
