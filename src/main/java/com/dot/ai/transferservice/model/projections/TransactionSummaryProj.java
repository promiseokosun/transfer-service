package com.dot.ai.transferservice.model.projections;

import java.math.BigDecimal;

public interface TransactionSummaryProj {
    Long getTotalCount();
    BigDecimal getTotalAmount();
    BigDecimal getTotalBilledAmount();

    BigDecimal getTotalCommission();
    BigDecimal getTotalTransactionFee();
}
