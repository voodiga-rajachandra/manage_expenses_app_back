package com.daurenassanbaev.firstpetproject.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegularPaymentDto {
    @NotNull(message = "Account ID is required")
    private Integer accountId;

    private String accountName;

    @NotBlank(message = "Type is required")
    private String type;

    @NotBlank(message = "Payment name is required")
    @Size(min = 3, max = 50, message = "Payment name must be between 3 and 50 characters")
    private String name;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    private String categoryName;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than or equal to 0.01")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency code must be exactly 3 characters")
    private String currency;

    private String description;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @NotNull(message = "Next due date is required")
    private LocalDateTime nextDueDate;

    @NotBlank(message = "Frequency is required")
    private String frequency;
}
