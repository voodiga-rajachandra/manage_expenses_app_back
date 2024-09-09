package com.daurenassanbaev.firstpetproject.database.repository;

import com.daurenassanbaev.firstpetproject.database.entity.AccountTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountTransferRepository extends JpaRepository<AccountTransfer, Integer> {
    @Query("select a from AccountTransfer a where a.fromAccount.owner.id=:accountId and a.toAccount.owner.id=:accountId")
    public List<AccountTransfer> findAllByUser(Integer accountId);
}
