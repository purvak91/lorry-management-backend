package com.example.lorryManagement.controller;

import com.example.lorryManagement.config.PaginationConfig;
import com.example.lorryManagement.dtos.LorryRequestDto;
import com.example.lorryManagement.dtos.LorryResponseDto;
import com.example.lorryManagement.entity.LorryEntity;
import com.example.lorryManagement.exception.BadRequestException;
import com.example.lorryManagement.mapper.LorryMapper;
import com.example.lorryManagement.service.LorryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

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
    @ApiResponse(responseCode = "201", description = "LR created successfully")
    @ApiResponse(responseCode = "409", description = "LR already exists")
    @ApiResponse(responseCode = "400", description = "Invalid request data")

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
    @ApiResponse(responseCode = "404", description = "LR does not exist")
    @ApiResponse(responseCode = "200", description = "LR fetched successfully")

    @GetMapping("/{lr}")
    public ResponseEntity<LorryResponseDto> getLorry(@PathVariable("lr") Long lr) {
        Optional<LorryEntity> entity = lorryService.findByLr(lr);
        return entity.map(lorryEntity -> ResponseEntity.ok(LorryMapper.toDto(lorryEntity))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Get paginated LR entries",
            description = """
                Returns a paginated list of LR records.
                You can filter by:
                - search term (search) matching LR number, consignor name, from/to locations, or lorry number
                - date range (fromDate / toDate)
                If no filters are provided, all LR entries are returned paginated and sorted in descending order.
                """
    )
    /*
     * API contract:
     * - Server-side pagination & filtering only
     * - Supported filters: search, from, to
     * - Sorting defaults to lr DESC
     * - Client must NOT apply local filtering
     */
    @GetMapping
    public ResponseEntity<Page<LorryResponseDto>> getAllLorries(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws BadRequestException {

        if (page < 0) {
            throw new BadRequestException("Page index must be >= 0");
        }

        if (size < 1 || size > PaginationConfig.MAX_PAGE_SIZE) {
            throw new BadRequestException(
                    "Page size must be between 1 and " + PaginationConfig.MAX_PAGE_SIZE
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "lr")
        );


        Page<LorryEntity> result = lorryService.findWithFilters(
                search,
                from,
                to,
                pageable
        );

        // mapping entities
        Page<LorryResponseDto> dtoPage =
                result.map(LorryMapper::toDto);

        return ResponseEntity.ok(dtoPage);
    }

    @Operation(
            summary = "Update an existing LR entry",
            description = "Updates non-null fields of an existing LR. Returns 404 if the LR does not exist."
    )
    @ApiResponse(responseCode = "200", description = "Updated successfully")
    @ApiResponse(responseCode = "400", description = "Path LR and body LR do not match")
    @ApiResponse(responseCode = "404", description = "LR does not exist")

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
    @ApiResponse(responseCode = "204", description = "LR deleted successfully")
    @ApiResponse(responseCode = "404", description = "LR not found")

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

    @GetMapping("/distinct/lorry-numbers")
    public List<String> getLorryNumbers() {
        return lorryService.getDistinctLorryNumbers();
    }

    @GetMapping("/distinct/from-locations")
    public List<String> getDistinctFromLocations() {
        return lorryService.getDistinctFromLocations();
    }

    @GetMapping("/distinct/to-locations")
    public List<String> getDistinctToLocations() {
        return lorryService.getDistinctToLocations();
    }

    @GetMapping("/distinct/consignors")
    public List<String> getDistinctConsignors() {
        return lorryService.getDistinctConsignorNames();
    }
}