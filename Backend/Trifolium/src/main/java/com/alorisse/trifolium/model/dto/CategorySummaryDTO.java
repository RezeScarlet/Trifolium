package com.alorisse.trifolium.model.dto;

import java.math.BigDecimal;

public record CategorySummaryDTO(
        String categoryTitle,
        String color,
        BigDecimal totalAmount
) {
    public CategorySummaryDTO(String categoryTitle, BigDecimal totalAmount) {
        this(categoryTitle, null, totalAmount != null ? totalAmount : BigDecimal.ZERO);
    }
}