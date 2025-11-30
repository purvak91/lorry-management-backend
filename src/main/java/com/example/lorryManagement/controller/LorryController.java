package com.example.lorryManagement.controller;

import com.example.lorryManagement.dtos.LorryRequestDto;
import com.example.lorryManagement.dtos.LorryResponseDto;
import com.example.lorryManagement.entity.LorryEntity;
import com.example.lorryManagement.mapper.LorryMapper;
import com.example.lorryManagement.service.LorryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<Page<LorryResponseDto>> getAllLorries(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(required = false) String lorryNumber,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            Pageable pageable) {
        Page<LorryEntity> page = lorryService.findAll(pageable);

        if (date != null) {
            page = lorryService.findByDate(date, pageable);
        }
        else if (lorryNumber != null && !lorryNumber.isBlank()) {
            page = lorryService.findByLorryNumber(lorryNumber, pageable);
        }
        else if (from != null || to != null) {
            page = lorryService.findByDateRange(
                    Objects.requireNonNullElseGet(from, () -> LocalDate.of(1900,1,1)),
                    Objects.requireNonNullElseGet(to, LocalDate::now),
                    pageable);
        }

        // mapping entities
        List<LorryResponseDto> dtos = page.getContent().stream().map(LorryMapper::toDto).collect(Collectors.toList());

        Page<LorryResponseDto> dtoPage = new PageImpl<>(dtos, pageable, page.getTotalElements());

        return  ResponseEntity.ok(dtoPage);
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