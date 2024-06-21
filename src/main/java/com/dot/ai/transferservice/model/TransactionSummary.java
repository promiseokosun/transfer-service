package com.dot.ai.transferservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "tbl_transaction_summary")
public class TransactionSummary extends BaseEntity<Long> {
    private Long totalCount;
    private BigDecimal totalAmount;
    private BigDecimal totalBilledAmount;
    private BigDecimal totalCommission;
    private BigDecimal totalTransactionFee;
}