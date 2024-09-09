package com.daurenassanbaev.firstpetproject.service;

import com.daurenassanbaev.firstpetproject.database.entity.AccountTransfer;
import com.daurenassanbaev.firstpetproject.database.repository.AccountTransferRepository;
import com.daurenassanbaev.firstpetproject.dto.AccountDto;
import com.daurenassanbaev.firstpetproject.dto.AccountTransferDto;
import com.daurenassanbaev.firstpetproject.dto.AccountTransferViewDto;
import com.daurenassanbaev.firstpetproject.mapper.AccountTransferDtoMapper;
import com.daurenassanbaev.firstpetproject.mapper.AccountTransferDtoViewMapper;
import com.daurenassanbaev.firstpetproject.mapper.AccountTransferMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AccountTransferService {
    private final AccountTransferRepository accountTransferRepository;
    private final AccountTransferMapper accountTransferMapper;
    private final AccountTransferDtoMapper accountTransferDtoMapper;
    private final AccountTransferDtoViewMapper accountTransferDtoViewMapper;
    private final AccountService accountService;
    private final CurrencyService currencyService;
    @Transactional
    public void save(AccountTransferDto accountTransferDto, String username) {
        AccountDto toAccountDto = accountService.findById(accountTransferDto.getToAccountId()).get();
        AccountDto fromAccountDto = accountService.findById(accountTransferDto.getFromAccountId()).get();
        String fromAccountDtoCurrency = fromAccountDto.getCurrency();
        String toAccountDtoCurrency = toAccountDto.getCurrency();
        String transferCurrency = accountTransferDto.getCurrency();
        BigDecimal transferAmount = accountTransferDto.getAmount();
        AccountTransfer accountTransfer = accountTransferMapper.map(accountTransferDto);

        // 1 logic
        if (!fromAccountDtoCurrency.equals(toAccountDtoCurrency)) {
            transferAmount = convertCurrency(transferAmount, transferCurrency, fromAccountDtoCurrency);
        }
        fromAccountDto.setBalance(fromAccountDto.getBalance().subtract(transferAmount));

        // 2 logic
        if (!toAccountDtoCurrency.equals(transferCurrency)) {
            transferAmount = convertCurrency(transferAmount, fromAccountDtoCurrency, toAccountDtoCurrency);
        }

        toAccountDto.setBalance(toAccountDto.getBalance().add(transferAmount));

        // update and save
        accountService.save(fromAccountDto, username);
        accountService.save(toAccountDto, username);
        accountTransferRepository.save(accountTransfer);
    }

    private BigDecimal convertCurrency(BigDecimal transferAmount, String fromCurrency, String toCurrency) {
        BigDecimal exchangeRate = BigDecimal.ONE;
        if (fromCurrency.equals("USD") && toCurrency.equals("KZT")) {
            exchangeRate = currencyService.getExchangeRate("USD");
        } else if (fromCurrency.equals("KZT") && toCurrency.equals("USD")) {
            exchangeRate = BigDecimal.valueOf(1).divide(currencyService.getExchangeRate("USD"), 4, RoundingMode.HALF_UP);
        } else if (fromCurrency.equals("EUR") && toCurrency.equals("KZT")) {
            exchangeRate = currencyService.getExchangeRate("EUR");
        } else if (fromCurrency.equals("KZT") && toCurrency.equals("EUR")) {
            exchangeRate = BigDecimal.valueOf(1).divide(currencyService.getExchangeRate("EUR"), 4, RoundingMode.HALF_UP);
        } else if (fromCurrency.equals("RUB") && toCurrency.equals("KZT")) {
            exchangeRate = currencyService.getExchangeRate("RUB");
        } else if (fromCurrency.equals("KZT") && toCurrency.equals("RUB")) {
            exchangeRate = BigDecimal.valueOf(1).divide(currencyService.getExchangeRate("RUB"), 4, RoundingMode.HALF_UP);
        }
        return transferAmount.multiply(exchangeRate);
    }

    public List<AccountTransferViewDto> findAll(Integer userId) {
        return accountTransferRepository.findAllByUser(userId).stream().map(accountTransfer -> accountTransferDtoViewMapper.map(accountTransfer)).toList();
    }

}
