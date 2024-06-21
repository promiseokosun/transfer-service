package com.dot.ai.transferservice.repo;

import com.dot.ai.transferservice.constant.Status;
import com.dot.ai.transferservice.model.Transaction;
import com.dot.ai.transferservice.model.projections.TransactionSummaryProj;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    @Query(value = " select * from tbl_transaction t where " +
            " ( :transactionReference is null or t.transaction_reference = :transactionReference ) and " +
            " ( :originatorAccountNumber is null or t.originator_account_number = :originatorAccountNumber ) and " +
            " ( :beneficiaryAccountNumber is null or t.beneficiary_account_number = :beneficiaryAccountNumber ) and " +
            " ( :status is null or t.status=:status ) and " +
            " ( :stateDate is null or t.created_date >= :stateDate ) and " +
            " ( :endDate is null or t.created_date <= :endDate ) ", nativeQuery = true)
    Page<Transaction> searchTransactions(String transactionReference,
                                         String originatorAccountNumber,
                                         String beneficiaryAccountNumber,
                                         String status,
                                         LocalDate stateDate,
                                         LocalDate endDate,
                                         Pageable pageable);

    List<Transaction> findAllByStatusAndHasBeenProcessed(Status status, boolean hasBeenProcessed);


    @Query(value = "select count(t.id) as totalCount , sum(t.amount) as totalAmount , sum(t.billed_amount) as totalBilledAmount, " +
            " sum(t.commission) as totalCommission, sum(t.transaction_fee) as totalTransactionFee " +
            " from tbl_transaction t  where t.has_been_processed = :hasBeenProccessed and " +
            " ( :searchDate is null or t.created_date = :searchDate ) " , nativeQuery = true)
    TransactionSummaryProj getTransactionSummary(LocalDate searchDate, boolean hasBeenProccessed);

    Optional<Transaction> findFirstByTransactionReference(String transactionRef);
}