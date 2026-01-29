package com.example.lorryManagement.dtos;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LorryRequestDto {
    @NotNull(message = "LR is missing. Try regenerating.")
    private Long lr;
    @NotBlank(message = "Lorry number is required")
    @Pattern(
            regexp = "^[A-Z]{2}\\d{1,2}[A-Z]{1,3}\\d{1,4}$",
            message = "Invalid lorry number format (e.g. MH12AB1234)"
    )
    private String lorryNumber;
    @NotBlank(message = "Consignor name is required")
    @Size(min = 3, max = 100, message = "Consignor name must be at least 3 characters")
    private String consignorName;
    @NotNull(message = "Please select a date")
    private LocalDate date;

    @NotBlank(message = "From location is required")
    @Size(min = 2, max = 100, message = "From location is too short")
    private String fromLocation;
    @NotBlank(message = "To location is required")
    @Size(min = 2, max = 100, message = "To location is too short")
    private String toLocation;

    @Size(max = 255, message = "Consignor address is too long")
    private String consignorAddress;

    @Size(max = 500, message = "Description is too long")
    private String description;

    @NotNull(message = "Weight is required")
    @Positive(message = "Weight must be greater than zero")
    private BigDecimal weight;

    @Positive(message = "Freight must be greater than zero")
    private BigDecimal freight;

}
