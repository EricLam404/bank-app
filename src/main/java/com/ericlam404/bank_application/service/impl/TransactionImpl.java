package com.ericlam404.bank_application.service.impl;

import com.ericlam404.bank_application.dto.TransactionDto;
import com.ericlam404.bank_application.entity.Transaction;
import com.ericlam404.bank_application.repository.TransactionRepository;
import com.ericlam404.bank_application.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionImpl implements TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction transaction = Transaction.builder()
                .transactionType((transactionDto.getType()))
                .accountNumber(transactionDto.getAccountNumber())
                .amount(transactionDto.getAmount())
                .status("SUCCESS")
                .build();

        transactionRepository.save(transaction);
        System.out.println("Transaction saved: " + transaction.getTransactionId());
    }
}
