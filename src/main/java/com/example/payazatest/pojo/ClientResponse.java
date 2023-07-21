package com.example.payazatest.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientResponse {
    ResponseDetails responseDetails = new ResponseDetails();
    private String accountName;
    private String accountNumber;
    private String operationReference;
    private String nipSessionID;
    private String tranDateTime;
    private String transactionAmount;
    private String transactionType;
    private String transactionSource;
    private String sourceAccountName;
    private String sourceBank;
    private String nameOfBank;


}
