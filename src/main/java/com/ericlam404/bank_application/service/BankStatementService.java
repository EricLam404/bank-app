package com.ericlam404.bank_application.service;

import com.ericlam404.bank_application.entity.Transaction;

import java.util.List;

public interface BankStatementService {

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate);
}
