package com.dot.ai.transferservice.dto.response;


import com.dot.ai.transferservice.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private String transactionReference;
    private BigDecimal amount;
    private BigDecimal transactionFee;
    private BigDecimal billedAmount;
    private Status status;
    private String statusMessage;
    private String description;
    private boolean hasCommission;
    private BigDecimal commission;

    private String originatorAccountNumber;
    private String originatorAccountName;
    private String originatorBankCode;
    private String originatorBankName;

    private String beneficiaryAccountNumber;
    private String beneficiaryAccountName;
    private String beneficiaryBankCode;
    private String beneficiaryBankName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
