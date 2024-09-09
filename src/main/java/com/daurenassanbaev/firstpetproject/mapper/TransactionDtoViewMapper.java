package com.daurenassanbaev.firstpetproject.mapper;

import com.daurenassanbaev.firstpetproject.database.entity.Transaction;
import com.daurenassanbaev.firstpetproject.dto.TransactionDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionViewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionDtoViewMapper implements Mapper<Transaction, TransactionViewDto> {

    @Override
    public TransactionViewDto map(Transaction transaction) {
        TransactionViewDto transactionDto = new TransactionViewDto();
        copy(transaction, transactionDto);
        return transactionDto;
    }
    private void copy(Transaction transaction, TransactionViewDto transactionDto) {
        transactionDto.setId(transaction.getId());
        transactionDto.setAccountName(transaction.getAccount().getAccountName());
        transactionDto.setTransactionDate(transaction.getTransactionDate());
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setCategoryName(transaction.getCategory().getCategoryName());
        transactionDto.setImagePath(transaction.getImage());
    }

}
