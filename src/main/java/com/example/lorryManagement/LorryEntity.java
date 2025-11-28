package com.example.lorryManagement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity(name = "lorry")
@Getter
@Setter
@AllArgsConstructor
public class LorryEntity {
    @Id
    private Long lr;
    @Column(nullable = false, length = 10)
    private String lorryNumber;
    @Column(nullable = false)
    private LocalDate date;
    private String fromLocation;
    private String toLocation;
    @Column(nullable = false)
    private String consignorName;
    private String consignorAddress;
    private String description;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal weight;
    @Column(precision = 10, scale = 2)
    private BigDecimal freight;
    public LorryEntity() {

    }
}
