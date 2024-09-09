package com.daurenassanbaev.firstpetproject.mapper;

import com.daurenassanbaev.firstpetproject.database.entity.Account;
import com.daurenassanbaev.firstpetproject.database.entity.Transaction;
import com.daurenassanbaev.firstpetproject.database.repository.AccountRepository;
import com.daurenassanbaev.firstpetproject.database.repository.CategoryRepository;
import com.daurenassanbaev.firstpetproject.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class TransactionDtoMapper implements Mapper<Transaction, TransactionDto> {
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionDto map(Transaction object) {
        TransactionDto transaction = new TransactionDto();
        copy(object, transaction);
        return transaction;
    }
    private void copy(Transaction object, TransactionDto transaction) {
        transaction.setTransactionDate(object.getTransactionDate());
        transaction.setAmount(object.getAmount());
        Integer accountId = object.getAccount().getId();
        Account account = accountRepository.findById(accountId).get();
        transaction.setAccountId(account.getId());
        transaction.setCategoryId(categoryRepository.findById(object.getCategory().getId()).get().getId());
        transaction.setDescription(object.getDescription());
        // тут кароче у меня в object get image дает строку а я хочу сохранить через маппирование в бд
        transaction.setImagePath(object.getImage());
    }

}
