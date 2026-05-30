package com.alorisse.trifolium.model.dto;

import java.math.BigDecimal;

public record GeneralSummaryDTO(
        BigDecimal totalIncome,
        BigDecimal totalExpenses,
        BigDecimal balance
) {}