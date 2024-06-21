package com.dot.ai.transferservice.model;

import com.dot.ai.transferservice.constant.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tbl_transaction")
public class Transaction extends BaseEntity<Long> {
    @Column(unique = true)
    private String transactionReference;
    private BigDecimal amount;
    private BigDecimal transactionFee;
    private BigDecimal billedAmount; // settlementAmount
    private BigDecimal commission;

    @Enumerated(EnumType.STRING)
    private Status status;
    private String statusMessage;
    @Column(columnDefinition = "TEXT")
    private String description;
    private boolean hasCommission;  // successful trx

    private String originatorAccountNumber;
    private String originatorAccountName;
    private String originatorBankCode;
    private String originatorBankName;

    private String beneficiaryAccountNumber;
    private String beneficiaryAccountName;
    private String beneficiaryBankCode;
    private String beneficiaryBankName;
    private boolean hasBeenProcessed;
}