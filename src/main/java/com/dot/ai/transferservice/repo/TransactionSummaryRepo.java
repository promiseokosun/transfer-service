package com.dot.ai.transferservice.repo;

import com.dot.ai.transferservice.model.TransactionSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface TransactionSummaryRepo extends JpaRepository<TransactionSummary, Long> {
    Optional<TransactionSummary> findByCreatedDate(LocalDate searchDate);
}