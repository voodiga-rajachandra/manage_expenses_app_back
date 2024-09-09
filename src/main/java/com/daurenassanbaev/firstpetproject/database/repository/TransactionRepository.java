package com.daurenassanbaev.firstpetproject.database.repository;

import com.daurenassanbaev.firstpetproject.database.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId")
    Page<Transaction> findAllByCategoryType(String type, Integer userId, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.amount BETWEEN :minAmount and :maxAmount and t.transactionDate between :fromDate and :toDate")
    Page<Transaction> findAllByCategoryType(String type, Integer userId, LocalDateTime fromDate, LocalDateTime toDate, BigDecimal minAmount, BigDecimal maxAmount, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.amount >= :minAmount and t.transactionDate between :fromDate and :toDate")
    Page<Transaction> findAllByCategoryTypeMin(String type, Integer userId, LocalDateTime fromDate, LocalDateTime toDate, BigDecimal minAmount, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.amount <= :maxAmount and t.transactionDate between :fromDate and :toDate")
    Page<Transaction> findAllByCategoryTypeMax(String type, Integer userId, LocalDateTime fromDate, LocalDateTime toDate, BigDecimal maxAmount, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.transactionDate between :fromDate and :toDate")
    Page<Transaction> findAllByCategoryType(String type, Integer userId, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.transactionDate >= :fromDate")
    Page<Transaction> findAllByCategoryTypeFromDate(String type, Integer userId, LocalDateTime fromDate, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.transactionDate <= :toDate")
    Page<Transaction> findAllByCategoryTypeToDate(String type, Integer userId, LocalDateTime toDate, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.amount >= :minAmount")
    Page<Transaction> findAllByCategoryTypeJustMin(String type, Integer userId, BigDecimal minAmount, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.amount <= :maxAmount")
    Page<Transaction> findAllByCategoryTypeJustMax(String type, Integer userId, BigDecimal maxAmount, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.transactionDate >= :fromDate")
    Page<Transaction> findAllByCategoryTypeJustFrom(String type, Integer userId, LocalDateTime fromDate, Pageable pageable);

    @Query("select t from Transaction t where t.category.type=:type and t.account.owner.id=:userId and t.transactionDate <= :toDate")
    Page<Transaction> findAllByCategoryTypeJustTo(String type, Integer userId, LocalDateTime toDate, Pageable pageable);

    List<Transaction> findAllByAccountId(Integer id);
}
