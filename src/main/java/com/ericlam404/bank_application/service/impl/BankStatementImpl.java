package com.ericlam404.bank_application.service.impl;

import com.ericlam404.bank_application.entity.Transaction;
import com.ericlam404.bank_application.repository.TransactionRepository;
import com.ericlam404.bank_application.service.BankStatementService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class BankStatementImpl implements BankStatementService {
    private TransactionRepository transactionRepository;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);

        List<Transaction> transactionsList = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isAfter(start.atStartOfDay()))
                .filter(transaction -> transaction.getCreatedAt().isBefore(end.plusDays(1).atStartOfDay()))
                .toList();
        return transactionsList;
    }
}
