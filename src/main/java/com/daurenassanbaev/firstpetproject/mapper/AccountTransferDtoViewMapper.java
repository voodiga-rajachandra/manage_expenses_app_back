package com.daurenassanbaev.firstpetproject.mapper;

import com.daurenassanbaev.firstpetproject.database.entity.AccountTransfer;
import com.daurenassanbaev.firstpetproject.database.entity.Transaction;
import com.daurenassanbaev.firstpetproject.dto.*;
import com.daurenassanbaev.firstpetproject.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountTransferDtoViewMapper implements Mapper<AccountTransfer, AccountTransferViewDto> {

    private final AccountService accountService;

    @Override
    public AccountTransferViewDto map(AccountTransfer accountTransfer) {
        AccountTransferViewDto accountTransferViewDto = new AccountTransferViewDto();
        copy(accountTransfer, accountTransferViewDto);
        return accountTransferViewDto;
    }
    private void copy(AccountTransfer accountTransfer, AccountTransferViewDto accountTransferViewDto) {
        accountTransferViewDto.setAmount(accountTransfer.getAmount());
        accountTransferViewDto.setCurrency(accountTransfer.getCurrency());
        accountTransferViewDto.setDescription(accountTransfer.getDescription());
        accountTransferViewDto.setTransferDate(accountTransfer.getTransferDate());
        AccountDto from = accountService.findById(accountTransfer.getFromAccount().getId()).get();
        AccountDto to = accountService.findById(accountTransfer.getToAccount().getId()).get();
        accountTransferViewDto.setFromAccountName(from.getAccountName());
        accountTransferViewDto.setToAccountName(to.getAccountName());
    }

}
