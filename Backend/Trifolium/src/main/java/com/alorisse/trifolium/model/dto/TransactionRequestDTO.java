package com.alorisse.trifolium.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record TransactionRequestDTO(
        BigDecimal amount,
        Instant dateTime,
        String title,
        String description,
        String type,
        Long categoryId) {
}
