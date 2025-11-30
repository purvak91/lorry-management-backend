package com.example.lorryManagement.controller;

import com.example.lorryManagement.dtos.LorryRequestDto;
import com.example.lorryManagement.dtos.LorryResponseDto;
import com.example.lorryManagement.entity.LorryEntity;
import com.example.lorryManagement.mapper.LorryMapper;
import com.example.lorryManagement.service.LorryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/lorry")
public class LorryController {
    private final LorryService lorryService;

    public LorryController(LorryService lorryService) {
        this.lorryService = lorryService;
    }

    @PostMapping
    public ResponseEntity<LorryResponseDto> addLorry(@Valid @RequestBody LorryRequestDto lorryRequestDto) {
        LorryEntity toSave = LorryMapper.toEntity(lorryRequestDto);
        LorryEntity saved = lorryService.save(toSave);
        return ResponseEntity.created(URI.create("/api/lorry/" + saved.getLr())).body(LorryMapper.toDto(saved));
    }

    @GetMapping("/{lr}")
    public ResponseEntity<LorryResponseDto> getLorry(@PathVariable("lr") Long lr) {
        Optional<LorryEntity> entity = lorryService.findByLr(lr);
        if (entity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(LorryMapper.toDto(entity.get()));
    }

    @GetMapping
    public ResponseEntity<List<LorryResponseDto>> getAllLorries() {
        List<LorryEntity> list = lorryService.findAll();
        List<LorryResponseDto> dtoList = new ArrayList<>();
        for (LorryEntity entity : list) {
            dtoList.add(LorryMapper.toDto(entity));
        }
        // TODO: pagination using pageable
        return ResponseEntity.ok(dtoList);
    }

    @PutMapping("/{lr}")
    public ResponseEntity<LorryResponseDto> updateLorry(@PathVariable Long lr, @Valid @RequestBody LorryRequestDto dto) {
        if (!Objects.equals(lr, dto.getLr())) {
            return ResponseEntity.badRequest().build();
        }
        if (lorryService.findByLr(lr).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        LorryEntity toUpdate = LorryMapper.toEntity(dto);
        LorryEntity updated = lorryService.update(toUpdate);
        return ResponseEntity.ok(LorryMapper.toDto(updated));
    }

    @DeleteMapping("/{lr}")
    public ResponseEntity<Void> deleteLorry(@PathVariable Long lr) {
        Optional<LorryEntity> entity = lorryService.findByLr(lr);
        if (entity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        lorryService.deleteByLr(entity.get().getLr());
        return ResponseEntity.noContent().build();
    }
}