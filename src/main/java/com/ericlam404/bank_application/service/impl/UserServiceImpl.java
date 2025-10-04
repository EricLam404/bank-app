package com.ericlam404.bank_application.service.impl;

import com.ericlam404.bank_application.dto.*;
import com.ericlam404.bank_application.entity.User;
import com.ericlam404.bank_application.repository.TransactionRepository;
import com.ericlam404.bank_application.repository.UserRepository;
import com.ericlam404.bank_application.service.EmailService;
import com.ericlam404.bank_application.service.TransactionService;
import com.ericlam404.bank_application.service.UserService;
import com.ericlam404.bank_application.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    TransactionService transactionService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .id(userRequest.getId())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        User savedUser = userRepository.save(newUser);
        // Send welcome email
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("Account Created Successfully")
                .messageBody("Dear " + savedUser.getFirstName() + " " + savedUser.getLastName() + ",\n\n" +
                        "Your account has been created successfully.\n" +
                        "Account Number: " + savedUser.getAccountNumber() + "\n" +
                        "Account Balance: $" + savedUser.getAccountBalance() + "\n\n" +
                        "Thank you for choosing our bank.\n\n" +
                        "Best regards,\n" +
                        "Bank Team")
                .build();
        emailService.sendEmailAlert(emailDetails);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo
                        .builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName())
                        .build()
                )
                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest request) {
        if (accountDoesNotExist(request.getAccountNumber())) {
            return accountNotExistResponse();
        }
        User user = userRepository.findByAccountNumber(request.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public String nameEnquiry(EnquiryRequest request) {
        if (accountDoesNotExist(request.getAccountNumber())) {
            return AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE;
        }
        User user = userRepository.findByAccountNumber(request.getAccountNumber());
        return user.getFirstName() + " " + user.getLastName();
    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest request) {
        if(accountDoesNotExist(request.getAccountNumber())) {
            return accountNotExistResponse();
        }
        User user = userRepository.findByAccountNumber(request.getAccountNumber());
        user.setAccountBalance(user.getAccountBalance().add(request.getAmount()));
        userRepository.save(user);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(user.getAccountNumber())
                .type("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDIT_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_CREDIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest request) {
        if(accountDoesNotExist(request.getAccountNumber())) {
            return accountNotExistResponse();
        }
        User user = userRepository.findByAccountNumber(request.getAccountNumber());
        if(user.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(user.getFirstName() + " " + user.getLastName())
                            .accountNumber(user.getAccountNumber())
                            .accountBalance(user.getAccountBalance())
                            .build())
                    .build();
        }
        user.setAccountBalance(user.getAccountBalance().subtract(request.getAmount()));
        userRepository.save(user);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(user.getAccountNumber())
                .type("DEBIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(transactionDto);
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBIT_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_DEBIT_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(user.getFirstName() + " " + user.getLastName())
                        .accountNumber(user.getAccountNumber())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public BankResponse transferFunds(TransferRequest request) {
        if(accountDoesNotExist(request.getFromAccountNumber()) || accountDoesNotExist(request.getToAccountNumber())) {
            return accountNotExistResponse();
        }
        User fromUser = userRepository.findByAccountNumber(request.getFromAccountNumber());
        User toUser = userRepository.findByAccountNumber(request.getToAccountNumber());
        if(fromUser.getAccountBalance().compareTo(request.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(fromUser.getFirstName() + " " + fromUser.getLastName())
                            .accountNumber(fromUser.getAccountNumber())
                            .accountBalance(fromUser.getAccountBalance())
                            .build())
                    .build();
        }
        fromUser.setAccountBalance(fromUser.getAccountBalance().subtract(request.getAmount()));
        toUser.setAccountBalance(toUser.getAccountBalance().add(request.getAmount()));

        User savedFromUser = userRepository.save(fromUser);
        User savedToUser = userRepository.save(toUser);

        TransactionDto debitTransactionDto = TransactionDto.builder()
                .accountNumber(savedFromUser.getAccountNumber())
                .type("DEBIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(debitTransactionDto);

        TransactionDto creditTransactionDto = TransactionDto.builder()
                .accountNumber(savedToUser.getAccountNumber())
                .type("CREDIT")
                .amount(request.getAmount())
                .build();
        transactionService.saveTransaction(creditTransactionDto);

        EmailDetails debitAlert = EmailDetails.builder()
                .recipient(savedFromUser.getEmail())
                .subject("Debit Alert")
                .messageBody("Dear " + savedFromUser.getFirstName() + " " + savedFromUser.getLastName() + ",\n\n" +
                        "An amount of $" + request.getAmount() + " has been debited from your account.\n" +
                        "Account Number: " + savedFromUser.getAccountNumber() + "\n" +
                        "New Account Balance: $" + savedFromUser.getAccountBalance() + "\n\n" +
                        "If you did not authorize this transaction, please contact us immediately.\n\n" +
                        "Best regards,\n" +
                        "Bank Team")
                .build();
        emailService.sendEmailAlert(debitAlert);

        EmailDetails creditAlert = EmailDetails.builder()
                .recipient(savedToUser.getEmail())
                .subject("Credit Alert")
                .messageBody("Dear " + savedToUser.getFirstName() + " " + savedToUser.getLastName() + ",\n\n" +
                        "An amount of $" + request.getAmount() + " has been credited to your account.\n" +
                        "Account Number: " + savedToUser.getAccountNumber() + "\n" +
                        "New Account Balance: $" + savedToUser.getAccountBalance() + "\n\n" +
                        "Thank you for banking with us.\n\n" +
                        "Best regards,\n" +
                        "Bank Team")
                .build();
        emailService.sendEmailAlert(creditAlert);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_CODE)
                .responseMessage(AccountUtils.ACCOUNT_TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(savedFromUser.getFirstName() + " " + savedFromUser.getLastName())
                        .accountNumber(savedFromUser.getAccountNumber())
                        .accountBalance(savedFromUser.getAccountBalance())
                        .build())
                .build();
    }

    public boolean accountDoesNotExist(String accountNumber) {
        return !userRepository.existsByAccountNumber(accountNumber);
    }

    public BankResponse accountNotExistResponse() {
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                .accountInfo(null)
                .build();
    }
}
