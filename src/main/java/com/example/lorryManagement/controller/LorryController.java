package com.example.lorryManagement.controller;

import com.example.lorryManagement.dtos.LorryRequestDto;
import com.example.lorryManagement.dtos.LorryResponseDto;
import com.example.lorryManagement.entity.LorryEntity;
import com.example.lorryManagement.mapper.LorryMapper;
import com.example.lorryManagement.service.LorryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lorry")
public class LorryController {
    private final LorryService lorryService;

    public LorryController(LorryService lorryService) {
        this.lorryService = lorryService;
    }

    @Operation(
            summary = "Create a new LR entry",
            description = "Creates a new lorry receipt with LR number, lorry details, consignor info, weight and freight. Returns 409 if the LR already exists."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "LR created successfully"),
            @ApiResponse(responseCode = "409", description = "LR already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public ResponseEntity<LorryResponseDto> addLorry(@Valid @RequestBody LorryRequestDto lorryRequestDto) {
        LorryEntity toSave = LorryMapper.toEntity(lorryRequestDto);
        LorryEntity saved = lorryService.save(toSave);
        return ResponseEntity.created(URI.create("/api/lorry/" + saved.getLr())).body(LorryMapper.toDto(saved));
    }

    @Operation(
            summary = "Get LR by its number",
            description = "Returns details of a single LR by its LR number. Returns 404 if not found."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "404", description = "LR does not exist"),
            @ApiResponse(responseCode = "200", description = "LR fetched successfully")
    })
    @GetMapping("/{lr}")
    public ResponseEntity<LorryResponseDto> getLorry(@PathVariable("lr") Long lr) {
        Optional<LorryEntity> entity = lorryService.findByLr(lr);
        if (entity.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(LorryMapper.toDto(entity.get()));
    }

    @Operation(
            summary = "Get paginated LR entries",
            description = """
                Returns a paginated list of LR records.
                You can filter by:
                - exact date (date)
                - lorry number (lorryNumber)
                - date range (fromDate / toDate)
                If no filters are provided, all LR entries are returned paginated.
                """
    )
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

    @Operation(
            summary = "Update an existing LR entry",
            description = "Updates non-null fields of an existing LR. Returns 404 if the LR does not exist."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated successfully"),
            @ApiResponse(responseCode = "400", description = "Path LR and body LR do not match"),
            @ApiResponse(responseCode = "404", description = "LR does not exist")
    })
    @PutMapping("/{lr}")
    public ResponseEntity<LorryResponseDto> updateLorry(@PathVariable Long lr, @Valid @RequestBody LorryRequestDto dto) {
        if (!Objects.equals(lr, dto.getLr())) {
            return ResponseEntity.badRequest().build();
        }
        LorryEntity toUpdate = LorryMapper.toEntity(dto);
        LorryEntity updated = lorryService.update(toUpdate);

        return ResponseEntity.ok(LorryMapper.toDto(updated));
    }

    @Operation(
            summary = "Delete an LR entry",
            description = "Deletes the LR with the given LR number. Returns 204 on success, 404 if the LR does not exist."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "LR deleted successfully"),
            @ApiResponse(responseCode = "404", description = "LR not found")
    })
    @DeleteMapping("/{lr}")
    public ResponseEntity<Void> deleteLorry(@PathVariable Long lr) {
        lorryService.deleteByLr(lr);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/next-lr")
    public Map<String, Long> getNextLr() {
        Long nextLr = lorryService.getNextLr();
        return Collections.singletonMap("lr", nextLr);
    }
}