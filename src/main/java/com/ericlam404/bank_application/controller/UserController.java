package com.ericlam404.bank_application.controller;

import com.ericlam404.bank_application.dto.*;
import com.ericlam404.bank_application.service.UserService;
import com.ericlam404.bank_application.utils.AccountUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserService userService;

    @Operation(
        summary = "Create a new bank account",
        description = "Creates a new bank account with user details."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE),
        @ApiResponse(responseCode = "400", description = AccountUtils.ACCOUNT_EXISTS_MESSAGE)
    })
    @PostMapping
    public BankResponse createAccount(@RequestBody UserRequest userRequest) {
        return userService.createAccount(userRequest);
    }

    @Operation(
        summary = "Get account balance",
        description = "Retrieves the balance for the specified account number."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = AccountUtils.ACCOUNT_FOUND_MESSAGE),
        @ApiResponse(responseCode = "404", description = AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
    })
    @GetMapping("/{accountNumber}/balance")
    public BankResponse getBalance(@PathVariable String accountNumber) {
        return userService.balanceEnquiry(new EnquiryRequest(accountNumber));
    }

    @Operation(
        summary = "Get account holder name",
        description = "Retrieves the account holder's name for the specified account number."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = AccountUtils.ACCOUNT_FOUND_MESSAGE),
        @ApiResponse(responseCode = "404", description = AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
    })
    @GetMapping("/{accountNumber}/name")
    public String getName(@PathVariable String accountNumber) {
        return userService.nameEnquiry(new EnquiryRequest(accountNumber));
    }

    @Operation(
        summary = "Credit account",
        description = "Credits the specified amount to the account."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE),
        @ApiResponse(responseCode = "404", description = AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
    })
    @PutMapping("/{accountNumber}/credit")
    public BankResponse creditAccount(@PathVariable String accountNumber, @RequestBody CreditDebitRequest request) {
        return userService.creditAccount(new CreditDebitRequest(accountNumber, request.getAmount()));
    }

    @Operation(
        summary = "Debit account",
        description = "Debits the specified amount from the account."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE),
        @ApiResponse(responseCode = "404", description = AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE),
        @ApiResponse(responseCode = "400", description = AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
    })
    @PutMapping("/{accountNumber}/debit")
    public BankResponse debitAccount(@PathVariable String accountNumber, @RequestBody CreditDebitRequest request) {
        return userService.debitAccount(new CreditDebitRequest(accountNumber, request.getAmount()));
    }

    @Operation(
        summary = "Transfer funds",
        description = "Transfers funds between two accounts."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = AccountUtils.ACCOUNT_TRANSFER_SUCCESS_MESSAGE),
        @ApiResponse(responseCode = "404", description = AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE),
        @ApiResponse(responseCode = "400", description = AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
    })
    @PutMapping("/transfer")
    public BankResponse transferFunds(@RequestBody TransferRequest request) {
        return userService.transferFunds(request);
    }

    @PostMapping("/login")
    public BankResponse login(@RequestBody LoginDto loginDto) {
        return userService.login(loginDto);
    }
}
