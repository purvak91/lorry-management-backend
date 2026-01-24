package com.example.lorryManagement.specification;

import com.example.lorryManagement.entity.LorryEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LorrySpecification {

    private LorrySpecification() {
        throw new IllegalStateException("Utility class");
    }

    public static Specification<LorryEntity> hasSearch(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isEmpty()) {
                return cb.conjunction();
            }
            String likeSearch = "%" + search.toLowerCase() + "%";
            return cb.or(
                cb.like(cb.lower(cb.toString(root.get("lr"))), likeSearch),
                cb.like(cb.lower(root.get("lorryNumber")), likeSearch),
                cb.like(cb.lower(root.get("consignorName")), likeSearch),
                cb.like(cb.lower(root.get("fromLocation")), likeSearch),
                cb.like(cb.lower(root.get("toLocation")), likeSearch)
            );
        };
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
