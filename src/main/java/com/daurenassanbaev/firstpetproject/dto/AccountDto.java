package com.daurenassanbaev.firstpetproject.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Integer id;

    @NotBlank(message = "Account name is required")
    @Size(min = 3, max = 30, message = "Account name must be between 3 and 30 characters")
    private String accountName;

    @NotNull(message = "Balance is required")
    @DecimalMin(value = "0.00", inclusive = true, message = "Balance must be greater than or equal to 0")
    private BigDecimal balance;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currency;
}
