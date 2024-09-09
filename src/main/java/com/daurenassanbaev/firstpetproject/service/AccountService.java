package com.daurenassanbaev.firstpetproject.service;

import com.daurenassanbaev.firstpetproject.database.entity.Account;
import com.daurenassanbaev.firstpetproject.database.entity.User;
import com.daurenassanbaev.firstpetproject.database.repository.AccountRepository;
import com.daurenassanbaev.firstpetproject.database.repository.UserJdbcRepository;
import com.daurenassanbaev.firstpetproject.database.repository.UserRepository;
import com.daurenassanbaev.firstpetproject.dto.AccountDto;
import com.daurenassanbaev.firstpetproject.validation.group.ExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final UserJdbcRepository userJdbcRepository;;

    @Transactional
    public void createMain(AccountDto accountDto, String username) {
        Account account = modelMapper.map(accountDto, Account.class);
        User user2 = userRepository.findByUsername(username).get();
        account.setOwner(user2);
        accountRepository.save(account);
        log.info("Created account {} by user {}", account.getAccountName(), user2.getUsername());
    }
    public Optional<AccountDto> findByOwnerMain(Integer userId) {
        Optional<Account> currentAccount = accountRepository.findByMain(userId);
        if (currentAccount.isPresent()) {
            ModelMapper modelMapper = new ModelMapper();
            AccountDto dto = modelMapper.map(currentAccount.get(), AccountDto.class);
            return Optional.of(dto);
        } else {
            return Optional.empty();
        }
    }
    public List<AccountDto> findByUserId(Integer userId) {
        return accountRepository.findByUserId(userId).stream().map(account -> modelMapper.map(account, AccountDto.class)).toList();
    }
    public List<AccountDto> findAll(Integer userId) {
        return accountRepository.findByUserId(userId).stream().map(account -> modelMapper.map(account, AccountDto.class)).toList();
    }
    public List<AccountDto> findAll() {
        return accountRepository.findAll().stream().map(m -> modelMapper.map(m, AccountDto.class)).toList();
    }
    @Transactional
    public Account create(AccountDto accountDto, String username) {
        Account account = modelMapper.map(accountDto, Account.class);
        var res = userRepository.findByUsername(username).get();
        // тут ошибка
        account.setOwner(res);
        var check = accountRepository.findByAccountName(accountDto.getAccountName(), username);
        if (check.isPresent()) {
            throw new ExistsException("Account with this name is exists");
        } else {
            accountRepository.save(account);
            return account;
        }

    }
    @Transactional
    public Account save(AccountDto accountDto, String username) {
        Account account = modelMapper.map(accountDto, Account.class);
        var res = userJdbcRepository.findByUsername(username);
        account.setOwner(res);
        accountRepository.save(account);
        return account;
    }
    public Optional<AccountDto> findById(Integer id) {
        return accountRepository.findById(id).map(account -> modelMapper.map(account, AccountDto.class));
    }
    @Transactional
    public Account update(AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        return accountRepository.save(account);
    }
    @Transactional
    public void delete(Integer id) {
        accountRepository.deleteById(id);
    }

}
