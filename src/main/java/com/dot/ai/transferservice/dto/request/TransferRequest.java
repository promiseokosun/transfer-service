package com.dot.ai.transferservice.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@ToString
@Getter
@RequiredArgsConstructor
public class TransferRequest {
    @NotBlank(message = "transactionReference is required")
    final private String transactionReference;
    @NotNull(message = "amount is required")
    final private BigDecimal amount;
    @NotBlank(message = "description is required")
    final private String description;
    @NotBlank(message = "originatorAccountNumber is required")
    @Size(min = 11, max = 11, message = "originatorAccountNumber should be 11 digits")
    final private String originatorAccountNumber;
    @NotBlank(message = "originatorAccountName is required")
    final private String originatorAccountName;
    @NotBlank(message = "originatorBankCode is required")
    final private String originatorBankCode;
    @NotBlank(message = "originatorBankName is required")
    final private String originatorBankName;
    @NotBlank(message = "beneficiaryAccountNumber is required")
    @Size(min = 11, max = 11, message = "beneficiaryAccountNumber should be 11 digits")
    final private String beneficiaryAccountNumber;
    @NotBlank(message = "beneficiaryAccountName is required")
    final private String beneficiaryAccountName;
    @NotBlank(message = "beneficiaryBankCode is required")
    final private String beneficiaryBankCode;
    @NotBlank(message = "beneficiaryBankName is required")
    final private String beneficiaryBankName;
}
