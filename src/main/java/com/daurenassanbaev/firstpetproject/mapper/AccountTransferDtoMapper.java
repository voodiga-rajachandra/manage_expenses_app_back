package com.daurenassanbaev.firstpetproject.mapper;

import com.daurenassanbaev.firstpetproject.database.entity.AccountTransfer;
import com.daurenassanbaev.firstpetproject.database.entity.Transaction;
import com.daurenassanbaev.firstpetproject.dto.AccountTransferDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionViewDto;
import com.daurenassanbaev.firstpetproject.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountTransferDtoMapper implements Mapper<AccountTransfer, AccountTransferDto> {

    private final AccountService accountService;

    @Override
    public AccountTransferDto map(AccountTransfer accountTransfer) {
        AccountTransferDto accountTransferDto= new AccountTransferDto();
        copy(accountTransfer, accountTransferDto);
        return accountTransferDto;
    }
    private void copy(AccountTransfer accountTransfer, AccountTransferDto accountTransferDto) {
        accountTransferDto.setId(accountTransfer.getId());
        accountTransferDto.setAmount(accountTransfer.getAmount());
        accountTransferDto.setCurrency(accountTransfer.getCurrency());
        accountTransferDto.setDescription(accountTransfer.getDescription());
        accountTransferDto.setTransferDate(accountTransfer.getTransferDate());
        accountTransferDto.setToAccountId(accountTransfer.getToAccount().getId());
        accountTransferDto.setFromAccountId(accountTransfer.getFromAccount().getId());
    }

}
