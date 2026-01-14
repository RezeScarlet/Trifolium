package com.alorisse.trifolium.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponseDTO(
        Long id,
        BigDecimal amount,
        Instant dateTime,
        String title,
        String description,
        String type,
        String categoryTitle,
        String categoryColor,
        String categoryIcon) {
}
