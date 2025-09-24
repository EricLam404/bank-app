package com.ericlam404.bank_application.controller;

import com.ericlam404.bank_application.dto.*;
import com.ericlam404.bank_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @GetMapping("/{accountNumber}/balance")
    public BankResponse getBalance(@PathVariable String accountNumber) {
        return userService.balanceEnquiry(new EnquiryRequest(accountNumber));
    }

    @GetMapping("/{accountNumber}/name")
    public String getName(@PathVariable String accountNumber) {
        return userService.nameEnquiry(new EnquiryRequest(accountNumber));
    }

    // Additional endpoints for credit and debit operations can be added here
    @PutMapping("/{accountNumber}/credit")
    public BankResponse creditAccount(@PathVariable String accountNumber, @RequestBody CreditDebitRequest request) {
        return userService.creditAccount(new CreditDebitRequest(accountNumber, request.getAmount()));
    }

    @PutMapping("/{accountNumber}/debit")
    public BankResponse debitAccount(@PathVariable String accountNumber, @RequestBody CreditDebitRequest request) {
        return userService.debitAccount(new CreditDebitRequest(accountNumber, request.getAmount()));
    }

    @PutMapping("/transfer")
    public BankResponse transferFunds(@RequestBody TransferRequest request) {
        return userService.transferFunds(request);
    }
}
