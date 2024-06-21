package com.dot.ai.transferservice.repo;

import com.dot.ai.transferservice.constant.Status;
import com.dot.ai.transferservice.model.Transaction;
import com.dot.ai.transferservice.model.projections.TransactionSummaryProj;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionRepoTest {

    @Autowired
    private TransactionRepo transactionRepo;
    private Transaction transaction1;
    private Transaction transaction2;
    private Transaction transaction3;
    private String beneficiaryAccountNumber1 = "0000";
    private String beneficiaryAccountName1 = "Tunde Mary";
    private String originatorAccountNumber1 = "1111";
    private String originatorAccountName1 = "Grace Monye";


    private String beneficiaryAccountNumber2 = "2222";
    private String beneficiaryAccountName2 = "Emma Wendy";
    private String originatorAccountNumber2 = "3333";
    private String originatorAccountName2 = "Sam Thomas";
    private String transactionRef = "RYTYYYYJK891";
    @BeforeAll
    void init() {
        transaction1 = Transaction.builder()
                .billedAmount(BigDecimal.valueOf(99.5))
                .amount(BigDecimal.valueOf(100))
                .transactionFee(BigDecimal.valueOf(0.5))
                .commission(BigDecimal.valueOf(0.001))
                .transactionReference(UUID.randomUUID().toString())
                .beneficiaryAccountNumber(beneficiaryAccountNumber1)
                .beneficiaryAccountName(beneficiaryAccountName1)
                .beneficiaryBankCode("053")
                .beneficiaryBankName("GTBANK")

                .originatorAccountNumber(originatorAccountNumber1)
                .originatorAccountName(originatorAccountName1)
                .originatorBankCode("053")
                .originatorBankName("GTBANK")
                .status(Status.SUCCESSFUL)
                .statusMessage(Status.SUCCESSFUL.getMessage())
                .description("Test Transfer")
                .build();
        transactionRepo.save(transaction1);

        transaction2 = Transaction.builder()
                .billedAmount(BigDecimal.valueOf(999.5))
                .amount(BigDecimal.valueOf(1000))
                .transactionFee(BigDecimal.valueOf(5))
                .commission(BigDecimal.valueOf(0.01))
                .transactionReference(transactionRef)
                .beneficiaryAccountNumber(beneficiaryAccountNumber2)
                .beneficiaryAccountName(beneficiaryAccountName2)
                .beneficiaryBankCode("053")
                .beneficiaryBankName("GTBANK")

                .originatorAccountNumber(originatorAccountNumber1)
                .originatorAccountName(originatorAccountName1)
                .originatorBankCode("053")
                .originatorBankName("GTBANK")
                .status(Status.FAILED)
                .statusMessage(Status.FAILED.getMessage())
                .description("Test Transfer")
                .build();
        transactionRepo.save(transaction2);

        transaction3 = Transaction.builder()
                .billedAmount(BigDecimal.valueOf(999.5))
                .amount(BigDecimal.valueOf(1000))
                .transactionFee(BigDecimal.valueOf(5))
                .commission(BigDecimal.valueOf(0.01))
                .transactionReference(UUID.randomUUID().toString())
                .beneficiaryAccountNumber(beneficiaryAccountNumber2)
                .beneficiaryAccountName(beneficiaryAccountName2)
                .beneficiaryBankCode("053")
                .beneficiaryBankName("GTBANK")

                .originatorAccountNumber(originatorAccountNumber1)
                .originatorAccountName(originatorAccountName1)
                .originatorBankCode("053")
                .originatorBankName("GTBANK")
                .status(Status.SUCCESSFUL)
                .statusMessage(Status.SUCCESSFUL.getMessage())
                .description("Test Transfer")
                .hasBeenProcessed(true)
                .build();
        transactionRepo.save(transaction3);
        log.info("{}", transaction1);
        log.info("{}", transaction2);

    }

    @Test
    void searchTransactions() {
        searchTransactionShouldReturnTransactionsWhenValidTransactionReferenceIsProvided();
        searchTransactionShouldReturnTransactionsWhenValidOriginatorAccountNumberIsProvided();
        searchTransactionShouldReturnTransactionsWhenValidBeneficiaryAccountNumberIsProvided();
        searchTransactionShouldReturnTransactionsWhenValidStatusIsProvided();
        searchTransactionShouldReturnTransactionsWhenValidDateRangeIsProvided();
        searchTransactionShouldReturnTransactionsWhenMoreThanOneValidParamIsProvided();
        searchTransactionShouldReturnEmptyListWhenInValidSearchParamIsProvided();
    }

    @Test
    void findAllByStatusAndHasBeenProcessed() {
        List<Transaction> t = transactionRepo.findAllByStatusAndHasBeenProcessed(Status.SUCCESSFUL, true);
        assertThat(t.size()).isGreaterThan(0);
    }

    @Test
    void getTransactionSummary() {
        getTransactionSummaryShouldReturnTransactionSummaryWhenValidDateIsProvided();
        getTransactionSummaryShouldZeroCountWhenInValidDateIsProvided();
    }

    private void getTransactionSummaryShouldReturnTransactionSummaryWhenValidDateIsProvided() {
        TransactionSummaryProj t = transactionRepo.getTransactionSummary(LocalDate.now(), true);
        log.info("totalCount: {}, totalAmount: {}, totalBilledAmount: {}, totalTransactionFee: {}, totalCommission: {}",
                t.getTotalCount(), t.getTotalAmount(), t.getTotalBilledAmount(), t.getTotalTransactionFee(), t.getTotalCommission());
        assertThat(t.getTotalCount()).isGreaterThan(0);
    }

    private void getTransactionSummaryShouldZeroCountWhenInValidDateIsProvided() {
        TransactionSummaryProj t = transactionRepo.getTransactionSummary(LocalDate.now().plusDays(2), true);
        log.info("totalCount: {}, totalAmount: {}, totalBilledAmount: {}, totalTransactionFee: {}, totalCommission: {}",
                t.getTotalCount(), t.getTotalAmount(), t.getTotalBilledAmount(), t.getTotalTransactionFee(), t.getTotalCommission());
        assertThat(t.getTotalCount()).isEqualTo(0);
    }


    private void searchTransactionShouldReturnEmptyListWhenInValidSearchParamIsProvided() {
        Page<Transaction> transactionPage = transactionRepo.searchTransactions("blahblah",
                null, null, null, null, null, Pageable.unpaged());
        assertThat(transactionPage.getTotalElements()).isEqualTo(0);
    }

    private void searchTransactionShouldReturnTransactionsWhenValidTransactionReferenceIsProvided() {
        Page<Transaction> transactionPage = transactionRepo.searchTransactions(transactionRef,
                null, null, null, null, null, Pageable.unpaged());
        assertThat(transactionPage.getTotalElements()).isGreaterThan(0);
    }

    private void searchTransactionShouldReturnTransactionsWhenValidOriginatorAccountNumberIsProvided() {
        Page<Transaction> transactionPage = transactionRepo.searchTransactions(null,
                originatorAccountNumber1, null, null, null, null, Pageable.unpaged());
        assertThat(transactionPage.getTotalElements()).isGreaterThan(0);
    }

    private void searchTransactionShouldReturnTransactionsWhenValidBeneficiaryAccountNumberIsProvided() {
        Page<Transaction> transactionPage = transactionRepo.searchTransactions(null,
                null, beneficiaryAccountNumber1, null, null, null, Pageable.unpaged());
        assertThat(transactionPage.getTotalElements()).isGreaterThan(0);
    }

    private void searchTransactionShouldReturnTransactionsWhenValidStatusIsProvided() {
        Page<Transaction> transactionPage = transactionRepo.searchTransactions(null,
                null, null, Status.FAILED.name(), null, null, Pageable.unpaged());
        assertThat(transactionPage.getTotalElements()).isGreaterThan(0);
    }

    private void searchTransactionShouldReturnTransactionsWhenValidDateRangeIsProvided() {
        Page<Transaction> transactionPage = transactionRepo.searchTransactions(null,
                null, null, null, LocalDate.now(), LocalDate.now(), Pageable.unpaged());
        assertThat(transactionPage.getTotalElements()).isGreaterThan(0);
    }

    private void searchTransactionShouldReturnTransactionsWhenMoreThanOneValidParamIsProvided() {
        Page<Transaction> transactionPage = transactionRepo.searchTransactions(transactionRef,
                null, beneficiaryAccountNumber2, Status.FAILED.name(), null, null, Pageable.unpaged());
        assertThat(transactionPage.getTotalElements()).isGreaterThan(0);
    }


}