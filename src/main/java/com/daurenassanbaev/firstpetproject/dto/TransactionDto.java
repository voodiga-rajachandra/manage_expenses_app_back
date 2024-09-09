package com.daurenassanbaev.firstpetproject.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Integer id;
    @NotNull(message = "Account ID is required")
    private Integer accountId;

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Amount must be greater than or equal to 0.01")
    private BigDecimal amount;

    @NotNull(message = "Transaction date is required")
    private LocalDateTime transactionDate;

    @Size(max = 255, message = "Description must be less than or equal to 255 characters")
    private String description;

    MultipartFile image;
    public TransactionDto(Integer id) {
        this.id = id;
    }
    private String imagePath;
}
