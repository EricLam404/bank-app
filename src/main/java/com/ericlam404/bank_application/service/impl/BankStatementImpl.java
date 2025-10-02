package com.ericlam404.bank_application.service.impl;

import com.ericlam404.bank_application.entity.Transaction;
import com.ericlam404.bank_application.entity.User;
import com.ericlam404.bank_application.repository.TransactionRepository;
import com.ericlam404.bank_application.repository.UserRepository;
import com.ericlam404.bank_application.service.BankStatementService;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
public class BankStatementImpl implements BankStatementService {
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionsList = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isAfter(start.atStartOfDay()))
                .filter(transaction -> transaction.getCreatedAt().isBefore(end.plusDays(1).atStartOfDay()))
                .toList();
        User user = userRepository.findByAccountNumber(accountNumber);

        String customerName = user.getFirstName() + " " + user.getLastName();


        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);

        System.out.println("setting size to " + statementSize.getWidth() + " x " + statementSize.getHeight());

        try {
            OutputStream outputStream = new FileOutputStream("statements/statement.pdf");
            PdfWriter.getInstance(document, outputStream);
            document.open();

            PdfPTable bankInfoTable = new PdfPTable(1);
            PdfPCell bankName = new PdfPCell(new Phrase("The Bank"));
            bankName.setBorder(0);
            bankName.setBackgroundColor(BaseColor.BLUE);
            bankName.setPadding(20f);

            PdfPCell bankAddress = new PdfPCell(new Phrase("123 Finance St, Money City, Country"));
            bankAddress.setBorder(0);
            bankInfoTable.addCell(bankName);
            bankInfoTable.addCell(bankAddress);

            PdfPTable statementInfo = new PdfPTable(2);
            PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
            customerInfo.setBorder(0);
            PdfPCell statement = new PdfPCell(new Phrase("ACCOUNT STATEMENT"));
            statement.setBorder(0);
            PdfPCell endDateCell = new PdfPCell(new Phrase("End Date: " + endDate));
            endDateCell.setBorder(0);

            PdfPCell name = new PdfPCell(new Phrase("Customer Name: " + customerName));
            name.setBorder(0);
            PdfPCell space = new PdfPCell();
            space.setBorder(0);

            PdfPCell address = new PdfPCell(new Phrase("Customer Address " + user.getAddress()));
            address.setBorder(0);

            PdfPTable transactionsTable = new PdfPTable(4);
            PdfPCell date = new PdfPCell((new Phrase("Date")));
            date.setBackgroundColor(BaseColor.LIGHT_GRAY);
            date.setBorder(0);
            PdfPCell transactionType = new PdfPCell((new Phrase("Transaction Type")));
            transactionType.setBackgroundColor(BaseColor.LIGHT_GRAY);
            transactionType.setBorder(0);
            PdfPCell amount = new PdfPCell((new Phrase("Amount")));
            amount.setBackgroundColor(BaseColor.LIGHT_GRAY);
            amount.setBorder(0);
            PdfPCell status = new PdfPCell((new Phrase("Status")));
            status.setBackgroundColor(BaseColor.LIGHT_GRAY);
            status.setBorder(0);

            transactionsTable.addCell(date);
            transactionsTable.addCell(transactionType);
            transactionsTable.addCell(amount);
            transactionsTable.addCell(status);

            transactionsList.forEach(transaction -> {
                transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
                transactionsTable.addCell(new Phrase(transaction.getTransactionType()));
                transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
                transactionsTable.addCell(new Phrase(transaction.getStatus()));
            });

            statementInfo.addCell(customerInfo);
            statementInfo.addCell(statement);
            statementInfo.addCell(endDateCell);
            statementInfo.addCell(name);
            statementInfo.addCell(space);
            statementInfo.addCell(address);

            document.add(bankInfoTable);
            document.add(statementInfo);
            document.add(transactionsTable);
            document.close();
            System.out.println("PDF generated successfully.");
        } catch (java.io.FileNotFoundException | com.itextpdf.text.DocumentException e) {
            e.printStackTrace();
        }

        return transactionsList;
    }
}
