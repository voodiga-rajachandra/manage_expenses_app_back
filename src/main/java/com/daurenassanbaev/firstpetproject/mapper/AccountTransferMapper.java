package com.daurenassanbaev.firstpetproject.mapper;

import com.daurenassanbaev.firstpetproject.database.entity.Account;
import com.daurenassanbaev.firstpetproject.database.entity.AccountTransfer;
import com.daurenassanbaev.firstpetproject.database.entity.Transaction;
import com.daurenassanbaev.firstpetproject.dto.AccountDto;
import com.daurenassanbaev.firstpetproject.dto.AccountTransferDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionViewDto;
import com.daurenassanbaev.firstpetproject.service.AccountService;
import com.daurenassanbaev.firstpetproject.service.AccountTransferService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountTransferMapper implements Mapper<AccountTransferDto, AccountTransfer> {

    private final AccountService accountService;
    private final ModelMapper modelMapper;

    @Override
    public AccountTransfer map(AccountTransferDto accountTransferDto) {
        AccountTransfer accountTransfer = new AccountTransfer();
        copy(accountTransferDto, accountTransfer);
        return accountTransfer;
    }
    private void copy(AccountTransferDto accountTransferDto, AccountTransfer accountTransfer) {
        AccountDto fromAccountDto = accountService.findById(accountTransferDto.getFromAccountId()).get();
        Account fromAccount = modelMapper.map(fromAccountDto, Account.class);
        accountTransfer.setFromAccount(fromAccount);
        accountTransfer.setTransferDate(accountTransferDto.getTransferDate());
        AccountDto toAccountDto = accountService.findById(accountTransferDto.getToAccountId()).get();
        Account toAccount = modelMapper.map(fromAccountDto, Account.class);
        accountTransfer.setToAccount(toAccount);
        accountTransfer.setAmount(accountTransferDto.getAmount());
        accountTransfer.setCurrency(accountTransferDto.getCurrency());
        accountTransfer.setDescription(accountTransferDto.getDescription());
    }

}
