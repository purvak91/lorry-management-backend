package com.example.lorryManagement.specification;

import com.example.lorryManagement.entity.LorryEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LorrySpecification {

    private LorrySpecification() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<LorryEntity> hasLorryNumber(String lorryNumber) {
        return (root, query, cb) ->
                lorryNumber == null || lorryNumber.isBlank()
                        ? null
                        : cb.equal(root.get("lorryNumber"), lorryNumber);
    }

    public static Specification<LorryEntity> hasDate(LocalDate date) {
        return (root, query, cb) ->
                date == null
                        ? null
                        : cb.equal(root.get("date"), date);
    }

    public static Specification<LorryEntity> hasDateBetween(LocalDate from, LocalDate to) {
        return (root, query, cb) -> {
            if (from == null && to == null) {
                return null;
            }
            if (from != null && to != null) {
                return cb.between(root.get("date"), from, to);
            }
            if (from != null) {
                return cb.greaterThanOrEqualTo(root.get("date"), from);
            }
            return cb.lessThanOrEqualTo(root.get("date"), to);
        };
    }
}
