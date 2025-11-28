package com.example.lorryManagement.service;

import com.example.lorryManagement.entity.LorryEntity;
import com.example.lorryManagement.repository.LorryRepository;
import jakarta.transaction.Transactional;
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
        return lorryRepository.save(lorryEntity);
    }

    @Override
    public Optional<LorryEntity> findByLr(Long lr) {
        return lorryRepository.findById(lr);
    }

    @Override
    public List<LorryEntity> findAll() {
        return lorryRepository.findAll();
    }

    @Override
    public void deleteByLr(Long lr) {
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
    public List<LorryEntity> findByDate(LocalDate date) {
        return lorryRepository.findByDate(date);
    }

    @Override
    public List<LorryEntity> findByLorryNumber(String lorryNumber) {
        return lorryRepository.findByLorryNumber(lorryNumber);
    }

    @Override
    public List<LorryEntity> findByConsignorName(String consignorName) {
        return lorryRepository.findByConsignorName(consignorName);
    }

    @Override
    public List<LorryEntity> findByDateRange(LocalDate start, LocalDate end) {
        return lorryRepository.findAllByDateBetween(start, end);
    }
}
