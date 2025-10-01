package com.ericlam404.bank_application.controller;

import com.ericlam404.bank_application.dto.BankResponse;
import com.ericlam404.bank_application.dto.UserRequest;
import com.ericlam404.bank_application.entity.Transaction;
import com.ericlam404.bank_application.service.BankStatementService;
import com.ericlam404.bank_application.service.UserService;
import com.ericlam404.bank_application.utils.AccountUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    BankStatementService bankStatementService;

    @Operation(
            summary = "Generate bank statement",
            description = "Generates a bank statement for the specified account number within the given date range."
    )
    @ApiResponses({
            @ApiResponse(responseCode = AccountUtils.BANK_STATEMENT_GENERATED_CODE, description = AccountUtils.BANK_STATEMENT_GENERATED_MESSAGE),
            @ApiResponse(responseCode = AccountUtils.ACCOUNT_EXISTS_CODE, description = AccountUtils.ACCOUNT_EXISTS_MESSAGE)
    })
    @GetMapping("/statement/{accountNumber}")
    public List<Transaction> generateBankStatement(@PathVariable String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate) {
        return bankStatementService.generateStatement(accountNumber, startDate, endDate);
    }
}
