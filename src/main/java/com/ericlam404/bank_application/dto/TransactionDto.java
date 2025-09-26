package com.ericlam404.bank_application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDto {
    private String transactionId;
    private String accountNumber;
    private String type;
    private BigDecimal amount;
    private String date;
}
