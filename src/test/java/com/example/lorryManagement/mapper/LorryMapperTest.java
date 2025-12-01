package com.example.lorryManagement.mapper;

import com.example.lorryManagement.dtos.LorryRequestDto;
import com.example.lorryManagement.dtos.LorryResponseDto;
import com.example.lorryManagement.entity.LorryEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LorryMapperTest {
    private final LorryMapper lorryMapper = new LorryMapper();

    @Test
    void toEntity_shouldMapAllFieldsFromRequestDto() {
        LorryRequestDto dto = new LorryRequestDto();
        dto.setLr(1111L);
        dto.setLorryNumber("MH12AB1234");
        dto.setDate(LocalDate.of(2024, 1, 15));
        dto.setFromLocation("Pune");
        dto.setToLocation("Mumbai");
        dto.setConsignorName("Alice");
        dto.setConsignorAddress("Some Address");
        dto.setWeight(BigDecimal.valueOf(1000));
        dto.setFreight(BigDecimal.valueOf(5000));
        dto.setDescription("Test load");

        LorryEntity entity = LorryMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getLr(), entity.getLr());
        assertEquals(dto.getLorryNumber(), entity.getLorryNumber());
        assertEquals(dto.getDate(), entity.getDate());
        assertEquals(dto.getFromLocation(), entity.getFromLocation());
        assertEquals(dto.getToLocation(), entity.getToLocation());
        assertEquals(dto.getConsignorName(), entity.getConsignorName());
        assertEquals(dto.getConsignorAddress(), entity.getConsignorAddress());
        assertEquals(dto.getWeight(), entity.getWeight());
        assertEquals(dto.getFreight(), entity.getFreight());
        assertEquals(dto.getDescription(), entity.getDescription());
    }


    @Test
    void toResponseDto_shouldMapAllFieldsFromEntity() {
        LorryEntity entity = new LorryEntity();
        entity.setLr(1111L);
        entity.setLorryNumber("MH12AB1234");
        entity.setDate(LocalDate.of(2024, 1, 15));
        entity.setFromLocation("Pune");
        entity.setToLocation("Mumbai");
        entity.setConsignorName("Alice");
        entity.setConsignorAddress("Some Address");
        entity.setWeight(BigDecimal.valueOf(1000));
        entity.setFreight(BigDecimal.valueOf(5000));
        entity.setDescription("Test load");

        LorryResponseDto dto = LorryMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getLr(), dto.getLr());
        assertEquals(entity.getLorryNumber(), dto.getLorryNumber());
        assertEquals(entity.getDate(), dto.getDate());
        assertEquals(entity.getFromLocation(), dto.getFromLocation());
        assertEquals(entity.getToLocation(), dto.getToLocation());
        assertEquals(entity.getConsignorName(), dto.getConsignorName());
        assertEquals(entity.getConsignorAddress(), dto.getConsignorAddress());
        assertEquals(entity.getWeight(), dto.getWeight());
        assertEquals(entity.getFreight(), dto.getFreight());
        assertEquals(entity.getDescription(), dto.getDescription());
    }

}
