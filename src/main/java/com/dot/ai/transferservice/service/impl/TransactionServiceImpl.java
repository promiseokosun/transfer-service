package com.dot.ai.transferservice.service.impl;

import com.dot.ai.transferservice.constant.Status;
import com.dot.ai.transferservice.dto.request.TransferRequest;
import com.dot.ai.transferservice.dto.response.TransactionResponse;
import com.dot.ai.transferservice.dto.response.TransactionSummaryResponse;
import com.dot.ai.transferservice.exception.CustomException;
import com.dot.ai.transferservice.model.Transaction;
import com.dot.ai.transferservice.model.TransactionSummary;
import com.dot.ai.transferservice.model.projections.TransactionSummaryProj;
import com.dot.ai.transferservice.repo.TransactionRepo;
import com.dot.ai.transferservice.repo.TransactionSummaryRepo;
import com.dot.ai.transferservice.service.PaymentProcessorService;
import com.dot.ai.transferservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepo transactionRepo;
    private final PaymentProcessorService paymentProcessorService;
    private final TransactionSummaryRepo transactionSummaryRepo;
    @Override
    public TransactionResponse transferFunds(TransferRequest request) {
        if(request.getAmount().doubleValue() <= 0) throw new CustomException("Amount cannot be zero or negative", HttpStatus.BAD_REQUEST);

        Optional<Transaction> optionalTransaction = transactionRepo.findFirstByTransactionReference(request.getTransactionReference());
        if(optionalTransaction.isPresent()) throw new CustomException("Duplicate transaction reference", HttpStatus.CONFLICT);

        Status status = paymentProcessorService.processTransfer(request);

        if(status.equals(Status.INSUFFICIENT_FUND)) throw new CustomException("Insufficient funds", HttpStatus.BAD_REQUEST);

        Transaction transaction = Transaction.builder()
                .amount(request.getAmount().setScale(2, RoundingMode.HALF_EVEN))
                .transactionReference(request.getTransactionReference())
                .description(request.getDescription())
                .originatorBankCode(request.getOriginatorBankCode())
                .originatorBankName(request.getOriginatorBankName())
                .originatorAccountNumber(request.getOriginatorAccountNumber())
                .originatorAccountName(request.getOriginatorAccountName())
                .beneficiaryBankCode(request.getBeneficiaryBankCode())
                .beneficiaryBankName(request.getBeneficiaryBankName())
                .beneficiaryAccountNumber(request.getBeneficiaryAccountNumber())
                .beneficiaryAccountName(request.getBeneficiaryAccountName())
                .status(status)
                .statusMessage(status.getMessage())
                .build();
        transactionRepo.save(transaction);
        return buildTransactionResponse(transaction);
    }

    @Override
    public void processSuccessfulTransactions() {
        List<Transaction> transactions = transactionRepo.findAllByStatusAndHasBeenProcessed(Status.SUCCESSFUL, false);

//            The commission on each successful transaction is 20% of the transaction fee
//            while the transaction fee is 0.5% of the original amount with a cap of 100
        for (Transaction transaction : transactions) {
            BigDecimal transactionFee = transaction.getAmount().multiply(BigDecimal.valueOf(0.5)).divide(BigDecimal.valueOf(100));
            transactionFee = transactionFee.compareTo(BigDecimal.valueOf(100)) > 0 ? BigDecimal.valueOf(100) : transactionFee;
            BigDecimal commission = transactionFee.multiply(BigDecimal.valueOf(20)).divide(BigDecimal.valueOf(100));
            BigDecimal billedAmount = transaction.getAmount().subtract(transactionFee);

            transaction.setAmount(transaction.getAmount().setScale(2, RoundingMode.HALF_EVEN));
            transaction.setBilledAmount(billedAmount.setScale(2, RoundingMode.HALF_EVEN));
            transaction.setCommission(commission.setScale(2, RoundingMode.HALF_EVEN));
            transaction.setTransactionFee(transactionFee.setScale(2, RoundingMode.HALF_EVEN));
            transaction.setHasBeenProcessed(true);
            transactionRepo.save(transaction);
        }
    }


    @Override
    public Page<TransactionResponse> searchTransactions(String transactionReference, String originatorAccountNumber, String beneficiaryAccountNumber,
                                                        String status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<Transaction> transactionPage = transactionRepo.searchTransactions(
                transactionReference, originatorAccountNumber, beneficiaryAccountNumber, status, startDate, endDate, pageable);

        List<TransactionResponse> transactionResponses =
                transactionPage.getContent().stream().map(this::buildTransactionResponse).collect(Collectors.toList());

        if (transactionPage.getContent().isEmpty()) {
            return new PageImpl<>(new ArrayList<>(), pageable, transactionPage.getTotalElements());
        }
        return new PageImpl<>(transactionResponses, pageable, transactionPage.getTotalElements());
    }

    @Override
    public TransactionSummaryProj getTransactionSummary(LocalDate searchDate) {
        if(searchDate.isAfter(LocalDate.now())) throw new CustomException("searchDate cannot be a future date", HttpStatus.BAD_REQUEST);
        return transactionRepo.getTransactionSummary(searchDate, true);
    }

    @Override
    public TransactionSummaryResponse getTransactionSummaryV2(LocalDate searchDate) {
        if(searchDate.isAfter(LocalDate.now())) throw new CustomException("searchDate cannot be a future date", HttpStatus.BAD_REQUEST);

        TransactionSummary transactionSummary = transactionSummaryRepo.findByCreatedDate(searchDate).orElseThrow(() ->
                new CustomException("Summary not found for this date", HttpStatus.NOT_FOUND));
        return buildTransactionSummaryResponse(transactionSummary);
    }

    private TransactionResponse buildTransactionResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .amount(transaction.getAmount())
                .transactionReference(transaction.getTransactionReference())
                .transactionFee(transaction.getTransactionFee())
                .amount(transaction.getAmount())
                .billedAmount(transaction.getBilledAmount())
                .description(transaction.getDescription())
                .beneficiaryAccountName(transaction.getBeneficiaryAccountName())
                .beneficiaryAccountNumber(transaction.getBeneficiaryAccountNumber())
                .beneficiaryBankCode(transaction.getBeneficiaryBankCode())
                .beneficiaryBankName(transaction.getBeneficiaryBankName())
                .originatorAccountName(transaction.getOriginatorAccountName())
                .originatorAccountNumber(transaction.getOriginatorAccountNumber())
                .originatorBankCode(transaction.getOriginatorBankCode())
                .originatorBankName(transaction.getOriginatorBankName())
                .commission(transaction.getCommission())
                .hasCommission(transaction.isHasCommission())
                .status(transaction.getStatus())
                .statusMessage(transaction.getStatusMessage())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    private TransactionSummaryResponse buildTransactionSummaryResponse(TransactionSummary summary) {
        return TransactionSummaryResponse.builder()
                .id(summary.getId())
                .totalCommission(summary.getTotalCommission())
                .totalTransactionFee(summary.getTotalTransactionFee())
                .totalBilledAmount(summary.getTotalBilledAmount())
                .totalAmount(summary.getTotalAmount())
                .totalCount(summary.getTotalCount())
                .createdAt(summary.getCreatedAt())
                .updatedAt(summary.getUpdatedAt())
                .build();
    }
}
