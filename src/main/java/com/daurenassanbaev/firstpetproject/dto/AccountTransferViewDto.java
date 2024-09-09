package com.daurenassanbaev.firstpetproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransferViewDto {
    private String fromAccountName;
    private String toAccountName;
    private BigDecimal amount;
    private LocalDateTime transferDate;
    private String description;
    private String currency;
}
