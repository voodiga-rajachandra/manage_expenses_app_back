package com.daurenassanbaev.firstpetproject.mapper;

import com.daurenassanbaev.firstpetproject.database.entity.Account;
import com.daurenassanbaev.firstpetproject.database.entity.RegularPayment;
import com.daurenassanbaev.firstpetproject.database.entity.Transaction;
import com.daurenassanbaev.firstpetproject.database.repository.AccountRepository;
import com.daurenassanbaev.firstpetproject.database.repository.CategoryRepository;
import com.daurenassanbaev.firstpetproject.dto.AccountDto;
import com.daurenassanbaev.firstpetproject.dto.RegularPaymentDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionDto;
import com.daurenassanbaev.firstpetproject.service.AccountService;
import com.daurenassanbaev.firstpetproject.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RegularPaymentMapper implements Mapper<RegularPaymentDto, RegularPayment> {
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    @Override
    public RegularPayment map(RegularPaymentDto regularPaymentDto) {
        RegularPayment regularPayment = new RegularPayment();
        copy(regularPaymentDto, regularPayment);
        return regularPayment;
    }
    private void copy(RegularPaymentDto regularPaymentDto, RegularPayment regularPayment) {
        regularPayment.setAccount(accountRepository.findById(regularPaymentDto.getAccountId()).get());
        regularPayment.setCategory(categoryRepository.findById(regularPaymentDto.getCategoryId()).get());
        regularPayment.setAmount(regularPaymentDto.getAmount());
        regularPayment.setDescription(regularPaymentDto.getDescription());
        regularPayment.setEndDate(regularPaymentDto.getEndDate());
        regularPayment.setStartDate(regularPaymentDto.getStartDate());
        regularPayment.setCurrency(regularPaymentDto.getCurrency());
        regularPayment.setName(regularPaymentDto.getName());
        regularPayment.setType(regularPaymentDto.getType());
        regularPayment.setNextDueDate(regularPaymentDto.getNextDueDate());
        regularPayment.setFrequency(regularPaymentDto.getFrequency());

    }

}
