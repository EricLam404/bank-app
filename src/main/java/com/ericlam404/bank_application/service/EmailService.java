package com.ericlam404.bank_application.service;

import com.ericlam404.bank_application.dto.EmailDetails;


public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
    void sendEmailWithAttachment(EmailDetails emailDetails);
}
