package com.dot.ai.transferservice.service;

import com.dot.ai.transferservice.dto.request.TransferRequest;
import com.dot.ai.transferservice.dto.response.TransactionResponse;
import com.dot.ai.transferservice.dto.response.TransactionSummaryResponse;
import com.dot.ai.transferservice.model.projections.TransactionSummaryProj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TransactionService {
    TransactionResponse transferFunds(TransferRequest request);

    void processSuccessfulTransactions();

    Page<TransactionResponse> searchTransactions(String transactionReference, String originatorAccountNumber, String beneficiaryAccountNumber,
                                                 String status, LocalDate startDate, LocalDate endDate, Pageable pageable);

    TransactionSummaryProj getTransactionSummary(LocalDate searchDate);

    TransactionSummaryResponse getTransactionSummaryV2(LocalDate searchDate);
}
