package com.example.lorryManagement.mapper;

import com.example.lorryManagement.dtos.LorryRequestDto;
import com.example.lorryManagement.dtos.LorryResponseDto;
import com.example.lorryManagement.entity.LorryEntity;

public final class LorryMapper {
    private LorryMapper() {

    }
    public static LorryEntity toEntity(LorryRequestDto dto) {
        LorryEntity entity = new LorryEntity();
        entity.setLr(dto.getLr());
        entity.setDate(dto.getDate());
        entity.setLorryNumber(dto.getLorryNumber());
        entity.setFreight(dto.getFreight());
        entity.setWeight(dto.getWeight());
        entity.setConsignorName(dto.getConsignorName());
        entity.setConsignorAddress(dto.getConsignorAddress());
        entity.setFromLocation(dto.getFromLocation());
        entity.setToLocation(dto.getToLocation());
        entity.setDescription(dto.getDescription());
        return entity;
    }
    public static LorryResponseDto toDto(LorryEntity entity) {
        LorryResponseDto dto = new LorryResponseDto();
        dto.setLr(entity.getLr());
        dto.setDate(entity.getDate());
        dto.setLorryNumber(entity.getLorryNumber());
        dto.setFreight(entity.getFreight());
        dto.setWeight(entity.getWeight());
        dto.setConsignorName(entity.getConsignorName());
        dto.setConsignorAddress(entity.getConsignorAddress());
        dto.setFromLocation(entity.getFromLocation());
        dto.setToLocation(entity.getToLocation());
        dto.setDescription(entity.getDescription());
        return dto;
    }
}
