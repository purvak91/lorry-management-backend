package com.example.lorryManagement.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LorryRequestDto {
    @NotNull @Min(1000) @Max(99999)
    private Long lr;
    @NotBlank @Size(min = 10, max = 10)
    private String lorryNumber;
    @NotBlank @Size(min = 3, max = 100)
    private String consignorName;
    @NotNull
    private LocalDate date;
    private String fromLocation;
    private String toLocation;
    private String consignorAddress;
    private String description;
    @Positive @NotNull
    private BigDecimal weight;
    @PositiveOrZero
    private BigDecimal freight;

}
