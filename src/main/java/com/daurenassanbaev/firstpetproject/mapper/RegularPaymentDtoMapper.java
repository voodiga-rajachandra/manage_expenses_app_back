package com.daurenassanbaev.firstpetproject.mapper;

import com.daurenassanbaev.firstpetproject.database.entity.Account;
import com.daurenassanbaev.firstpetproject.database.entity.Category;
import com.daurenassanbaev.firstpetproject.database.entity.RegularPayment;
import com.daurenassanbaev.firstpetproject.database.entity.Transaction;
import com.daurenassanbaev.firstpetproject.dto.AccountDto;
import com.daurenassanbaev.firstpetproject.dto.RegularPaymentDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegularPaymentDtoMapper implements Mapper<RegularPayment, RegularPaymentDto> {

    @Override
    public RegularPaymentDto map(RegularPayment regularPayment) {
        RegularPaymentDto regularPaymentDto = new RegularPaymentDto();
        copy(regularPayment, regularPaymentDto);
        return regularPaymentDto;
    }
    private void copy(RegularPayment regularPayment, RegularPaymentDto regularPaymentDto) {
        Account account = regularPayment.getAccount();
        Category category = regularPayment.getCategory();
        regularPaymentDto.setDescription(regularPayment.getDescription());
        regularPaymentDto.setAmount(regularPayment.getAmount());
        regularPaymentDto.setAccountName(account.getAccountName());
        regularPaymentDto.setCategoryName(category.getCategoryName());
        regularPaymentDto.setCurrency(regularPayment.getCurrency());
        regularPaymentDto.setName(regularPayment.getName());
        regularPaymentDto.setEndDate(regularPayment.getEndDate());
        regularPaymentDto.setStartDate(regularPayment.getStartDate());
        regularPaymentDto.setNextDueDate(regularPayment.getNextDueDate());
        regularPaymentDto.setFrequency(regularPayment.getFrequency());
        regularPaymentDto.setType(regularPayment.getType());
        regularPaymentDto.setAccountId(account.getId());
        regularPaymentDto.setCategoryId(category.getId());
    }

}
