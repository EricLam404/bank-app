package com.ericlam404.bank_application.service;

import com.ericlam404.bank_application.dto.BankResponse;
import com.ericlam404.bank_application.dto.UserRequest;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);

}
