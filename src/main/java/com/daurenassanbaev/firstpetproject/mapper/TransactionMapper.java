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
public class TransactionMapper implements Mapper<TransactionDto, Transaction> {
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Transaction map(TransactionDto object) {
        Transaction transaction = new Transaction();
        copy(object, transaction);
        return transaction;
    }
    private void copy(TransactionDto object, Transaction transaction) {
        transaction.setTransactionDate(object.getTransactionDate());
        transaction.setAmount(object.getAmount());
        Integer accountId = object.getAccountId();
        Account account = accountRepository.findById(accountId).get();
        transaction.setAccount(account);
        transaction.setCategory(categoryRepository.findById(object.getCategoryId()).get());
        transaction.setDescription(object.getDescription());
        System.out.println(object.getImage().getOriginalFilename());
        Optional.ofNullable(object.getImage()).filter(Predicate.not(MultipartFile::isEmpty)).ifPresent(image -> transaction.setImage(image.getOriginalFilename()));
    }

}
