package com.dot.ai.transferservice.dto.response;


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
public class TransactionSummaryResponse {
    private Long id;
    private Long totalCount;
    private BigDecimal totalAmount;
    private BigDecimal totalBilledAmount;
    private BigDecimal totalCommission;
    private BigDecimal totalTransactionFee;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
