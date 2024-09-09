package com.daurenassanbaev.firstpetproject.database.repository;

import com.daurenassanbaev.firstpetproject.database.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Query("select a from Account a where a.owner.id = :userId and a.accountName = 'Main'")
    Optional<Account> findByMain(Integer userId);

    @Query("select a from Account a where a.owner.id = :userId")
    List<Account> findByUserId(Integer userId);

    @Query("select a from Account a where a.accountName=:name and a.owner.username=:username")
    Optional<Account> findByAccountName(String name, String username);

}
