package com.dot.ai.transferservice.scheduler;

import com.dot.ai.transferservice.model.TransactionSummary;
import com.dot.ai.transferservice.model.projections.TransactionSummaryProj;
import com.dot.ai.transferservice.repo.TransactionRepo;
import com.dot.ai.transferservice.repo.TransactionSummaryRepo;
import com.dot.ai.transferservice.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionScheduler {

    private final TransactionRepo transactionRepo;
    private final TransactionSummaryRepo transactionSummaryRepo;
    private final TransactionService transactionService;
    @Scheduled(cron = "00 00 01 * * *", zone = "Africa/Lagos")
    public void createTransactionSummary() {
        TransactionSummaryProj summary = transactionRepo.getTransactionSummary(LocalDate.now(), true);
        if(summary.getTotalCount() > 0) {
            TransactionSummary transactionSummary = TransactionSummary.builder()
                    .totalAmount(summary.getTotalAmount())
                    .totalBilledAmount(summary.getTotalBilledAmount())
                    .totalCommission(summary.getTotalCommission())
                    .totalCount(summary.getTotalCount())
                    .totalTransactionFee(summary.getTotalTransactionFee())
                    .build();
            transactionSummaryRepo.save(transactionSummary);
            log.info("Transaction summary created successfully: {}", transactionSummary);
        }
    }

    @Scheduled(cron = "59 59 23 * * *", zone = "Africa/Lagos") // end of the day
    public void scheduleJob() {
        transactionService.processSuccessfulTransactions();
    }
}
