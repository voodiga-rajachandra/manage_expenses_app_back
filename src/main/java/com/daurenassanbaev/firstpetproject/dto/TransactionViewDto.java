package com.daurenassanbaev.firstpetproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionViewDto {
    private Integer id;
    private String accountName;
    private String categoryName;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String description;
    MultipartFile image;
    private String imagePath;
}
