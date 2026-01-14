package com.alorisse.trifolium.model.dto;

import com.alorisse.trifolium.model.entity.Category;
import com.alorisse.trifolium.model.entity.User;
import com.alorisse.trifolium.model.enums.Type;

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
