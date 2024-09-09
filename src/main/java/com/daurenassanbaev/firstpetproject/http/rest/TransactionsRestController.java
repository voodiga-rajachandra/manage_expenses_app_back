package com.daurenassanbaev.firstpetproject.http.rest;


import com.daurenassanbaev.firstpetproject.database.entity.Account;
import com.daurenassanbaev.firstpetproject.dto.AccountDto;
import com.daurenassanbaev.firstpetproject.dto.TransactionDto;
import com.daurenassanbaev.firstpetproject.service.AccountService;
import com.daurenassanbaev.firstpetproject.service.TransactionService;
import com.daurenassanbaev.firstpetproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionsRestController {
    private final TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<List<TransactionDto>> findAll(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(transactionService.findAllByAccountId(id));
    }

    @GetMapping("/transaction/{id}")
    public ResponseEntity<TransactionDto> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @GetMapping(value = "/{id}/image")
    public ResponseEntity<byte[]> findImage(@PathVariable("id") Long id) {
        return transactionService.findImage(id).map(content -> ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_OCTET_STREAM_VALUE).contentLength(content.length).body(content)).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionDto create(@RequestBody TransactionDto transactionDto) {
        return transactionService.create(transactionDto);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void update(Integer id, @RequestBody TransactionDto transactionDto) {
        transactionService.update(transactionDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<? > delete(@PathVariable("id") Integer id) {
        return transactionService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
