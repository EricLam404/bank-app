package com.ericlam404.bank_application.utils;

import java.time.Year;

public class AccountUtils {
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "Account with provided email already exists";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Account created successfully";

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
