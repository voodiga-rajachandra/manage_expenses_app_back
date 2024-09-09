package com.daurenassanbaev.firstpetproject.service;

import com.daurenassanbaev.firstpetproject.database.entity.Account;
import com.daurenassanbaev.firstpetproject.database.entity.RegularPayment;
import com.daurenassanbaev.firstpetproject.database.repository.AccountRepository;
import com.daurenassanbaev.firstpetproject.database.repository.RegularPaymentRepository;
import com.daurenassanbaev.firstpetproject.dto.RegularPaymentDto;
import com.daurenassanbaev.firstpetproject.mapper.RegularPaymentDtoMapper;
import com.daurenassanbaev.firstpetproject.mapper.RegularPaymentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RegularPaymentsService {
    private final RegularPaymentRepository regularPaymentRepository;
    private final RegularPaymentDtoMapper regularPaymentDtoMapper;
    private final RegularPaymentMapper regularPaymentMapper;
    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final CurrencyService currencyService;

    public List<RegularPaymentDto> findAllByUserId(Integer userId) {
        return regularPaymentRepository.findAllByUserId(userId).stream()
                .map(regularPaymentDtoMapper::map).toList();
    }

    @Transactional
    public void save(RegularPaymentDto regularPaymentDto) {
        RegularPayment regularPayment = regularPaymentMapper.map(regularPaymentDto);
        regularPaymentRepository.save(regularPayment);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void progressExpense() {
        List<RegularPayment> list = regularPaymentRepository.findAllByNextDueDateAndType(LocalDateTime.now(), "expense");
        for (RegularPayment regularPayment : list) {
            if (!subtractMoney(regularPayment)) {
                break;
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void progressIncome() {
        List<RegularPayment> list = regularPaymentRepository.findAllByNextDueDateAndType(LocalDateTime.now(), "income");
        for (RegularPayment regularPayment : list) {
            addingMoney(regularPayment);
        }
    }

    private boolean subtractMoney(RegularPayment regularPayment) {
        Account account = regularPayment.getAccount();
        BigDecimal DOLLAR_EXCHANGE_RATE = currencyService.getExchangeRate("USD"); // Курс тенге к доллару
        BigDecimal RUB_EXCHANGE_RATE = currencyService.getExchangeRate("RUB"); // Курс тенге к доллару
        BigDecimal EUR_EXCHANGE_RATE = currencyService.getExchangeRate("EUR"); // Курс тенге к доллару
        BigDecimal regularPaymentAmount = regularPayment.getAmount();

        if (account.getBalance().compareTo(regularPaymentAmount) < 0) {
            return false;
        } else {
            if (account.getCurrency().equals(regularPayment.getCurrency())) {
                account.setBalance(account.getBalance().subtract(regularPaymentAmount));
            } else if (account.getCurrency().equals("USD") && regularPayment.getCurrency().equals("KZT")) {
                // Конвертация KZT -> USD
                account.setBalance(account.getBalance().subtract(regularPaymentAmount.divide(DOLLAR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
            } else if (account.getCurrency().equals("KZT") && regularPayment.getCurrency().equals("USD")) {
                // Конвертация USD -> KZT
                account.setBalance(account.getBalance().subtract(regularPaymentAmount.multiply(DOLLAR_EXCHANGE_RATE)));
            } else if (account.getCurrency().equals("EUR") && regularPayment.getCurrency().equals("KZT")) {
                // Конвертация KZT -> EUR
                account.setBalance(account.getBalance().subtract(regularPaymentAmount.divide(EUR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
            } else if (account.getCurrency().equals("KZT") && regularPayment.getCurrency().equals("EUR")) {
                // Конвертация EUR -> KZT
                account.setBalance(account.getBalance().subtract(regularPaymentAmount.multiply(EUR_EXCHANGE_RATE)));
            } else if (account.getCurrency().equals("RUB") && regularPayment.getCurrency().equals("KZT")) {
                // Конвертация KZT -> RUB
                account.setBalance(account.getBalance().subtract(regularPaymentAmount.divide(RUB_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
            } else if (account.getCurrency().equals("KZT") && regularPayment.getCurrency().equals("RUB")) {
                // Конвертация RUB -> KZT
                account.setBalance(account.getBalance().subtract(regularPaymentAmount.multiply(RUB_EXCHANGE_RATE)));
            } else if (account.getCurrency().equals("USD") && regularPayment.getCurrency().equals("EUR")) {
                // Конвертация EUR -> USD через тенге
                BigDecimal eurToKzt = regularPaymentAmount.multiply(EUR_EXCHANGE_RATE);
                account.setBalance(account.getBalance().subtract(eurToKzt.divide(DOLLAR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
            } else if (account.getCurrency().equals("EUR") && regularPayment.getCurrency().equals("USD")) {
                // Конвертация USD -> EUR через тенге
                BigDecimal usdToKzt = regularPaymentAmount.multiply(DOLLAR_EXCHANGE_RATE);
                account.setBalance(account.getBalance().subtract(usdToKzt.divide(EUR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
            } else if (account.getCurrency().equals("USD") && regularPayment.getCurrency().equals("RUB")) {
                // Конвертация RUB -> USD через тенге
                BigDecimal rubToKzt = regularPaymentAmount.multiply(RUB_EXCHANGE_RATE);
                account.setBalance(account.getBalance().subtract(rubToKzt.divide(DOLLAR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
            } else if (account.getCurrency().equals("RUB") && regularPayment.getCurrency().equals("USD")) {
                // Конвертация USD -> RUB через тенге
                BigDecimal usdToKzt = regularPaymentAmount.multiply(DOLLAR_EXCHANGE_RATE);
                account.setBalance(account.getBalance().subtract(usdToKzt.divide(RUB_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
            } else if (account.getCurrency().equals("EUR") && regularPayment.getCurrency().equals("RUB")) {
                // Конвертация RUB -> EUR через тенге
                BigDecimal rubToKzt = regularPaymentAmount.multiply(RUB_EXCHANGE_RATE);
                account.setBalance(account.getBalance().subtract(rubToKzt.divide(EUR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
            } else if (account.getCurrency().equals("RUB") && regularPayment.getCurrency().equals("EUR")) {
                // Конвертация EUR -> RUB через тенге
                BigDecimal eurToKzt = regularPaymentAmount.multiply(EUR_EXCHANGE_RATE);
                account.setBalance(account.getBalance().subtract(eurToKzt.divide(RUB_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
            }
            if (regularPayment.getFrequency().equals("daily")) {
                regularPayment.setNextDueDate(regularPayment.getNextDueDate().plusDays(1));
            } else if (regularPayment.getFrequency().equals("weekly")) {
                regularPayment.setNextDueDate(regularPayment.getNextDueDate().plusWeeks(1));
            } else if (regularPayment.getFrequency().equals("monthly")) {
                regularPayment.setNextDueDate(regularPayment.getNextDueDate().plusMonths(1));
            } else if (regularPayment.getFrequency().equals("yearly")) {
                regularPayment.setNextDueDate(regularPayment.getNextDueDate().plusYears(1));
            }
            accountRepository.save(account);
            regularPaymentRepository.save(regularPayment);
            return true;
        }
    }
    private void addingMoney(RegularPayment regularPayment) {
        Account account = regularPayment.getAccount();
        BigDecimal DOLLAR_EXCHANGE_RATE = BigDecimal.valueOf(483.32); // Курс тенге к доллару
        BigDecimal RUB_EXCHANGE_RATE = BigDecimal.valueOf(5.54); // Курс тенге к доллару
        BigDecimal EUR_EXCHANGE_RATE = BigDecimal.valueOf(533.52); // Курс тенге к доллару
        BigDecimal regularPaymentAmount = regularPayment.getAmount();

        if (account.getCurrency().equals(regularPayment.getCurrency())) {
            account.setBalance(account.getBalance().add(regularPaymentAmount));
        } else if (account.getCurrency().equals("USD") && regularPayment.getCurrency().equals("KZT")) {
            // Конвертация KZT -> USD
            account.setBalance(account.getBalance().add(regularPaymentAmount.divide(DOLLAR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
        } else if (account.getCurrency().equals("KZT") && regularPayment.getCurrency().equals("USD")) {
            // Конвертация USD -> KZT
            account.setBalance(account.getBalance().add(regularPaymentAmount.multiply(DOLLAR_EXCHANGE_RATE)));
        } else if (account.getCurrency().equals("EUR") && regularPayment.getCurrency().equals("KZT")) {
            // Конвертация KZT -> EUR
            account.setBalance(account.getBalance().add(regularPaymentAmount.divide(EUR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
        } else if (account.getCurrency().equals("KZT") && regularPayment.getCurrency().equals("EUR")) {
            // Конвертация EUR -> KZT
            account.setBalance(account.getBalance().add(regularPaymentAmount.multiply(EUR_EXCHANGE_RATE)));
        } else if (account.getCurrency().equals("RUB") && regularPayment.getCurrency().equals("KZT")) {
            // Конвертация KZT -> RUB
            account.setBalance(account.getBalance().add(regularPaymentAmount.divide(RUB_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
        } else if (account.getCurrency().equals("KZT") && regularPayment.getCurrency().equals("RUB")) {
            // Конвертация RUB -> KZT
            account.setBalance(account.getBalance().add(regularPaymentAmount.multiply(RUB_EXCHANGE_RATE)));
        } else if (account.getCurrency().equals("USD") && regularPayment.getCurrency().equals("EUR")) {
            // Конвертация EUR -> USD через тенге
            BigDecimal eurToKzt = regularPaymentAmount.multiply(EUR_EXCHANGE_RATE);
            account.setBalance(account.getBalance().add(eurToKzt.divide(DOLLAR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
        } else if (account.getCurrency().equals("EUR") && regularPayment.getCurrency().equals("USD")) {
            // Конвертация USD -> EUR через тенге
            BigDecimal usdToKzt = regularPaymentAmount.multiply(DOLLAR_EXCHANGE_RATE);
            account.setBalance(account.getBalance().add(usdToKzt.divide(EUR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
        } else if (account.getCurrency().equals("USD") && regularPayment.getCurrency().equals("RUB")) {
            // Конвертация RUB -> USD через тенге
            BigDecimal rubToKzt = regularPaymentAmount.multiply(RUB_EXCHANGE_RATE);
            account.setBalance(account.getBalance().add(rubToKzt.divide(DOLLAR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
        } else if (account.getCurrency().equals("RUB") && regularPayment.getCurrency().equals("USD")) {
            // Конвертация USD -> RUB через тенге
            BigDecimal usdToKzt = regularPaymentAmount.multiply(DOLLAR_EXCHANGE_RATE);
            account.setBalance(account.getBalance().add(usdToKzt.divide(RUB_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
        } else if (account.getCurrency().equals("EUR") && regularPayment.getCurrency().equals("RUB")) {
            // Конвертация RUB -> EUR через тенге
            BigDecimal rubToKzt = regularPaymentAmount.multiply(RUB_EXCHANGE_RATE);
            account.setBalance(account.getBalance().add(rubToKzt.divide(EUR_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
        } else if (account.getCurrency().equals("RUB") && regularPayment.getCurrency().equals("EUR")) {
            // Конвертация EUR -> RUB через тенге
            BigDecimal eurToKzt = regularPaymentAmount.multiply(EUR_EXCHANGE_RATE);
            account.setBalance(account.getBalance().add(eurToKzt.divide(RUB_EXCHANGE_RATE, BigDecimal.ROUND_HALF_UP)));
        }
        if (regularPayment.getFrequency().equals("daily")) {
            regularPayment.setNextDueDate(regularPayment.getNextDueDate().plusDays(1));
        } else if (regularPayment.getFrequency().equals("weekly")) {
            regularPayment.setNextDueDate(regularPayment.getNextDueDate().plusWeeks(1));
        } else if (regularPayment.getFrequency().equals("monthly")) {
            regularPayment.setNextDueDate(regularPayment.getNextDueDate().plusMonths(1));
        } else if (regularPayment.getFrequency().equals("yearly")) {
            regularPayment.setNextDueDate(regularPayment.getNextDueDate().plusYears(1));
            accountRepository.save(account);
            regularPaymentRepository.save(regularPayment);
        }
    }

}
