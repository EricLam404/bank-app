package com.ericlam404.bank_application.service;

import com.ericlam404.bank_application.dto.TransactionDto;

public interface TransactionService {
    void saveTransaction(TransactionDto transaction);
}
