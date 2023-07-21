package com.example.payazatest.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientRequest {
    private RequestHeader requestHeader = new RequestHeader();
    private String referenceNumber;
    private String customerName;
    private String customerID;
    private String bvn;
    private BigDecimal transactionAmount;

}
