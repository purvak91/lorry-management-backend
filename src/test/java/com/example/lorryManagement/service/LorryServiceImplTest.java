package com.example.lorryManagement.service;

import com.example.lorryManagement.entity.LorryEntity;
import com.example.lorryManagement.repository.LorryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LorryServiceImplTest {
    @InjectMocks
    private LorryServiceImpl lorryService;

    @Mock
    private LorryRepository lorryRepository;

    @Test
    void save_whenLrDoesNotExist_shouldSaveAndReturnEntity() {
        LorryEntity input = new LorryEntity();
        input.setLr(1111L);

        LorryEntity saved = new LorryEntity();
        saved.setLr(1111L);

        when(lorryRepository.existsById(1111L)).thenReturn(false);
        when(lorryRepository.save(input)).thenReturn(saved);

        LorryEntity result = lorryService.save(input);

        assertEquals(saved, result);
        verify(lorryRepository).existsById(1111L);
        verify(lorryRepository).save(input);
    }

    @Test
    void save_whenLrAlreadyExists_shouldThrowDuplicateKeyException() {
        LorryEntity input = new LorryEntity();
        input.setLr(1111L);

        when(lorryRepository.existsById(1111L)).thenReturn(true);

        assertThrows(DuplicateKeyException.class, () -> lorryService.save(input));

        verify(lorryRepository).existsById(1111L);
        verify(lorryRepository, never()).save(any());
    }

    @Test
    void update_whenLrNotFound_shouldThrowNoSuchElementException() {
        LorryEntity input = new LorryEntity();
        input.setLr(1111L);

        when(lorryRepository.findById(1111L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> lorryService.update(input));

        verify(lorryRepository).findById(1111L);
        verify(lorryRepository, never()).save(any());
    }

    @Test
    void update_whenLrIsNull_shouldThrowIllegalArgumentException() {
        LorryEntity input = new LorryEntity();
        input.setLr(null);

        assertThrows(IllegalArgumentException.class, () -> lorryService.update(input));

        verify(lorryRepository, never()).findById(any());
        verify(lorryRepository, never()).save(any());
    }

    @Test
    void update_whenLrFound_shouldSaveAndReturnEntity() {
        LorryEntity existing = new LorryEntity();
        existing.setLr(1111L);
        existing.setLorryNumber("OLD123");

        LorryEntity input = new LorryEntity();
        input.setLr(1111L);
        input.setLorryNumber("NEW123");

        LorryEntity saved = new LorryEntity();
        saved.setLr(1111L);
        saved.setLorryNumber("NEW123");

        when(lorryRepository.findById(1111L)).thenReturn(Optional.of(existing));
        when(lorryRepository.save(existing)).thenReturn(saved);

        LorryEntity result = lorryService.update(input);

        assertEquals("NEW123", result.getLorryNumber());

        verify(lorryRepository).findById(1111L);
        verify(lorryRepository).save(existing);
    }

    @Test
    void update_whenLrFound_shouldUpdateOnlyNonNullFields() {
        LorryEntity existing = new LorryEntity();
        existing.setLr(1111L);
        existing.setLorryNumber("OLD123");
        existing.setWeight(BigDecimal.valueOf(100));

        LorryEntity input = new LorryEntity();
        input.setLr(1111L);
        input.setLorryNumber("NEW123");

        LorryEntity saved = new LorryEntity();
        saved.setLr(1111L);
        saved.setLorryNumber("NEW123");
        saved.setWeight(BigDecimal.valueOf(100));

        when(lorryRepository.findById(1111L)).thenReturn(Optional.of(existing));
        when(lorryRepository.save(existing)).thenReturn(saved);

        LorryEntity result = lorryService.update(input);
        assertEquals("NEW123", result.getLorryNumber());
        assertEquals(BigDecimal.valueOf(100), result.getWeight());
        assertNull(input.getWeight());

        verify(lorryRepository).findById(1111L);
        verify(lorryRepository).save(existing);
    }

    @Test
    void findByDate_shouldDelegateToRepositoryAndReturnPage() {
        LocalDate date = LocalDate.of(2024, 1, 1);
        Pageable pageable = PageRequest.of(0, 5);

        LorryEntity lorry1 = new LorryEntity();
        lorry1.setLr(1111L);
        lorry1.setDate(date);

        LorryEntity lorry2 = new LorryEntity();
        lorry2.setLr(2222L);
        lorry2.setDate(date);

        List<LorryEntity> lorries = List.of(lorry1, lorry2);
        Page<LorryEntity> page = new PageImpl<>(lorries, pageable, lorries.size());

        when(lorryRepository.findByDate(date, pageable)).thenReturn(page);

        Page<LorryEntity> result = lorryService.findByDate(date, pageable);

        assertEquals(page, result);
        assertEquals(2, result.getContent().size());
        assertEquals(lorries, result.getContent());

        verify(lorryRepository).findByDate(date, pageable);
    }


    @Test
    void findByLorryNumber_shouldDelegateToRepositoryAndReturnPage() {
        String lorryNumber = "OLD123";
        Pageable pageable = PageRequest.of(0, 5);

        LorryEntity lorry1 = new LorryEntity();
        lorry1.setLr(1111L);
        lorry1.setLorryNumber(lorryNumber);

        LorryEntity lorry2 = new LorryEntity();
        lorry2.setLr(2222L);
        lorry2.setLorryNumber(lorryNumber);

        LorryEntity lorry3 = new LorryEntity();
        lorry3.setLr(3333L);
        lorry3.setLorryNumber("OLD222");

        List<LorryEntity> lorries = List.of(lorry1, lorry2);
        Page<LorryEntity> page = new  PageImpl<>(lorries, pageable, lorries.size());

        when(lorryRepository.findByLorryNumber(lorryNumber, pageable)).thenReturn(page);

        Page<LorryEntity> result = lorryService.findByLorryNumber(lorryNumber, pageable);

        assertEquals(page, result);
        assertEquals(2, result.getContent().size());
        assertEquals(lorries, result.getContent());

        verify(lorryRepository).findByLorryNumber(lorryNumber, pageable);
    }

    @Test
    void findByConsignorName_shouldDelegateToRepositoryAndReturnPage() {
        String consignorName = "Alice";
        Pageable pageable = PageRequest.of(0, 5);

        LorryEntity lorry1 = new LorryEntity();
        lorry1.setLr(1111L);
        lorry1.setConsignorName(consignorName);

        LorryEntity lorry2 = new LorryEntity();
        lorry2.setLr(2222L);
        lorry2.setConsignorName("Bob");

        LorryEntity lorry3 = new LorryEntity();
        lorry3.setLr(3333L);
        lorry3.setConsignorName(consignorName);

        List<LorryEntity> lorries = List.of(lorry1, lorry3);
        Page<LorryEntity> page = new  PageImpl<>(lorries, pageable, lorries.size());

        when(lorryRepository.findByConsignorName(consignorName, pageable)).thenReturn(page);

        Page<LorryEntity> result = lorryService.findByConsignorName(consignorName, pageable);

        assertEquals(page, result);
        assertEquals(2, result.getContent().size());
        assertEquals(lorries, result.getContent());

        verify(lorryRepository).findByConsignorName(consignorName, pageable);
    }

    @Test
    void findByDateRange_shouldDelegateToRepositoryAndReturnPage() {
        LocalDate start = LocalDate.of(2020, 1, 1);
        LocalDate end = LocalDate.of(2020, 12, 31);
        Pageable pageable = PageRequest.of(0, 5);

        LorryEntity lorry1 = new LorryEntity();
        lorry1.setLr(1111L);
        lorry1.setDate(LocalDate.of(2020, 5, 20));

        LorryEntity lorry2 = new LorryEntity();
        lorry2.setLr(2222L);
        lorry2.setDate(LocalDate.of(2021, 5, 20));

        LorryEntity lorry3 = new LorryEntity();
        lorry3.setLr(3333L);
        lorry3.setDate(LocalDate.of(2020, 12, 20));

        LorryEntity lorry4 = new LorryEntity();
        lorry4.setLr(4444L);
        lorry4.setDate(LocalDate.of(2020, 1, 20));

        LorryEntity lorry5 = new LorryEntity();
        lorry5.setLr(5555L);
        lorry5.setDate(LocalDate.of(2018, 12, 20));

        List<LorryEntity> lorries = List.of(lorry1, lorry3, lorry4);
        Page<LorryEntity> page = new  PageImpl<>(lorries, pageable, lorries.size());

        when(lorryRepository.findAllByDateBetween(start, end, pageable)).thenReturn(page);

        Page<LorryEntity> result = lorryService.findByDateRange(start, end, pageable);

        assertEquals(page, result);
        assertEquals(3, result.getContent().size());
        assertEquals(lorries, result.getContent());

        verify(lorryRepository).findAllByDateBetween(start, end, pageable);
    }

    @Test
    void deleteByLr_shouldCallRepositoryDeleteById() {
        Long lr = 1111L;
        when(lorryRepository.existsById(lr)).thenReturn(true);

        lorryService.deleteByLr(lr);
        verify(lorryRepository).deleteById(lr);
    }

}
