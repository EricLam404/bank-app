package com.ericlam404.bank_application.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "Account with provided email already exists";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created successfully";

    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "Account with provided account number does not exist";

    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account found successfully";

    public static final String ACCOUNT_CREDIT_SUCCESS_CODE = "005";
    public static final String ACCOUNT_CREDIT_SUCCESS_MESSAGE = "Account credited successfully";

    public static final String ACCOUNT_DEBIT_SUCCESS_CODE = "006";
    public static final String ACCOUNT_DEBIT_SUCCESS_MESSAGE = "Account debited successfully";

    public static final String INSUFFICIENT_BALANCE_CODE = "007";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient balance to debit";

    public static final String ACCOUNT_TRANSFER_SUCCESS_CODE = "008";
    public static final String ACCOUNT_TRANSFER_SUCCESS_MESSAGE = "Transfer completed successfully";

    public static String generateAccountNumber() {
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        // generate random number between min and max
        int randNumber = (int) (Math.random() * (max - min + 1) + min);

        // convert the current and random number to string and concatenate them

        return String.valueOf(currentYear.getValue()) + String.valueOf(randNumber);
    }
}
