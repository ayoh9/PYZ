package com.example.payazatest.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "trans_log_details")
public class TransactionLog {
    @Id
    @Column(name = "reference_number")
    private String transactionReference;

    @Column(name = "tenant_id")
    private String tenantID;

    @Column(name = "customer_id")
    private String customerID;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "account_number")
    private String accountNumber;

    //Reserved or Dynamic
    @Column(name = "account_type")
    private String accountType;

    @Column(name = "bvn_number")
    private String bvnNumber;

    @Column(name = "response_message")
    private String responseMessage;

    @Column(name = "response_code")
    private String responseCode;

    @Column(name = "operation_reference")
    private String operation_reference;

    @Column(name = "transaction_date_time")
    private String transactionDateTime;

    @Column(name = "transaction_completion_time")
    private String transactionCompletionTime;

    @Column(name = "account_status")
    private String accountStatus;

    @Column(name = "transaction_amount")
    private String transactionAmount;

    @Column(name = "bank_name")
    private String bankName;

    //Column used for search. This is important to minimise runtime
    @Column(name = "transaction_date")
    private Date transactionDate;
}
