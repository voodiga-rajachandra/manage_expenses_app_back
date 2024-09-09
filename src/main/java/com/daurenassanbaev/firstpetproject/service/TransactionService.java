package com.daurenassanbaev.firstpetproject.service;


import com.daurenassanbaev.firstpetproject.database.entity.Account;
import com.daurenassanbaev.firstpetproject.database.entity.Transaction;
import com.daurenassanbaev.firstpetproject.database.repository.TransactionRepository;
import com.daurenassanbaev.firstpetproject.dto.TransactionDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionViewDto;
import com.daurenassanbaev.firstpetproject.mapper.TransactionDtoMapper;
import com.daurenassanbaev.firstpetproject.mapper.TransactionDtoViewMapper;
import com.daurenassanbaev.firstpetproject.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionDtoViewMapper transactionDtoViewMapper;
    private final ImageService imageService;
    private final TransactionDtoMapper transactionDtoMapper;

    @Transactional
    public void saveExpense(TransactionDto transactionDto) {
        uploadImage(transactionDto.getImage());
        Transaction transaction = transactionMapper.map(transactionDto);
        transactionRepository.save(transaction);
        Account account = transaction.getAccount();
        var currentBalance = account.getBalance();
        var sum = transaction.getAmount();
        var result = currentBalance.subtract(sum);
        account.setBalance(result);
    }

    public TransactionDto create(TransactionDto transactionDto) {
        Transaction transaction = transactionRepository.save(transactionMapper.map(transactionDto));
        return transactionDtoMapper.map(transaction);
    }
    @SneakyThrows
    private void uploadImage(MultipartFile image) {
        if (!image.isEmpty()) {
            imageService.uploadImage(image.getOriginalFilename(), image.getInputStream());
        }
    }

    public Optional<byte[]> findImage(Long id) {
        return transactionRepository.findById(id).map(Transaction::getImage).filter(StringUtils::hasText).flatMap(imageService::getImage);
    }

    @Transactional
    public void saveIncome(TransactionDto transactionDto) {
        uploadImage(transactionDto.getImage());
        Transaction transaction = transactionMapper.map(transactionDto);
        transactionRepository.save(transaction);
        Account account = transaction.getAccount();
        var currentBalance = account.getBalance();
        var sum = transaction.getAmount();
        var result = currentBalance.add(sum);
        account.setBalance(result);

    }

    public TransactionDto findById(Integer id) {
        return transactionDtoMapper.map(transactionRepository.findById(Long.valueOf(id)).get());
    }

    public List<TransactionDto> findAllByAccountId(Integer accountId) {
        return transactionRepository.findAllByAccountId(accountId).stream().map(transactionDtoMapper::map).toList();
    }
    public Page<TransactionViewDto> findAllByCategoryName(String name, Integer userId, LocalDateTime fromDate, LocalDateTime toDate, BigDecimal minAmount, BigDecimal maxAmount, String sortQuery, String accountName, PageRequest pageRequest) {
        Page<Transaction> transactions = null;
        Sort sort = Sort.unsorted();
        if (sortQuery != null) {
            String[] values = sortQuery.split(","); // [amount, asc] or [date, desc]
            String field = values[0];
            String order = values[1];
            var sortBy = Sort.sort(Transaction.class);
            if (field.equals("amount")) {
                sort = Sort.by("amount");
            } else if (field.equals("date")) {
                sort = sortBy.by(Transaction::getTransactionDate);
            } if (order != null) {
                if (order.equals("desc")) {
                    sort = sort.descending();
                } else if (order.equals("asc")) {
                    sort = sort.ascending();;
                }
            }
        }
        pageRequest = PageRequest.of(pageRequest.getPageNumber(), pageRequest.getPageSize(), sort);
        if (fromDate != null && toDate != null && minAmount != null && maxAmount != null) {
            transactions = transactionRepository.findAllByCategoryType(name, userId, fromDate, toDate, minAmount, maxAmount, pageRequest);
        } else if (fromDate != null && toDate != null && minAmount != null && maxAmount == null) {
            transactions = transactionRepository.findAllByCategoryTypeMin(name, userId, fromDate, toDate, minAmount, pageRequest);
        } else if (fromDate != null && toDate != null && minAmount == null && maxAmount != null) {
            transactions = transactionRepository.findAllByCategoryTypeMax(name, userId, fromDate, toDate, maxAmount, pageRequest);
        } else if (fromDate != null && toDate != null && minAmount == null && maxAmount == null) {
            transactions = transactionRepository.findAllByCategoryType(name, userId, fromDate, toDate, pageRequest);
        } else if (fromDate != null && toDate == null && minAmount != null && maxAmount != null) {
            transactions = transactionRepository.findAllByCategoryTypeFromDate(name, userId, fromDate, pageRequest);
        } else if (fromDate == null && toDate != null && minAmount != null && maxAmount != null) {
            transactions = transactionRepository.findAllByCategoryTypeToDate(name, userId, toDate, pageRequest);
        } else if (minAmount != null) {
            transactions = transactionRepository.findAllByCategoryTypeJustMin(name, userId, minAmount, pageRequest);
        } else if (fromDate != null) {
            transactions = transactionRepository.findAllByCategoryTypeJustFrom(name, userId, fromDate, pageRequest);
        } else if (toDate != null) {
            transactions = transactionRepository.findAllByCategoryTypeJustTo(name, userId, toDate, pageRequest);
        } else if (maxAmount != null) {
            transactions = transactionRepository.findAllByCategoryTypeJustMax(name, userId, maxAmount, pageRequest);
        } else {
            transactions = transactionRepository.findAllByCategoryType(name, userId, pageRequest);
        }

        return transactions.map(transactionDtoViewMapper::map);
    }
    public List<TransactionDto> findAll() {
        return transactionRepository.findAll().stream().map(transactionDtoMapper::map).toList();
    }

    public boolean delete(Integer id) {
        transactionRepository.deleteById(Long.valueOf(id));
        return true;
    }

    public void update(TransactionDto transactionDto) {
        transactionRepository.save(transactionMapper.map(transactionDto));
    }
}
