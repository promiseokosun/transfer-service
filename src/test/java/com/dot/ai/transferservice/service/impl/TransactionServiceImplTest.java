package com.dot.ai.transferservice.service.impl;

import com.dot.ai.transferservice.constant.Status;
import com.dot.ai.transferservice.dto.request.TransferRequest;
import com.dot.ai.transferservice.model.Transaction;
import com.dot.ai.transferservice.repo.TransactionRepo;
import com.dot.ai.transferservice.service.PaymentProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.dot.ai.transferservice.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepo transactionRepo;
    @Mock
    private PaymentProcessorService paymentProcessorService;
    private Transaction transactionMock;
    private TransferRequest transactionRequestMock;
    private List<Transaction> transactionListMock = new ArrayList<>();

    private Page<Transaction> transactionPageMock;

    @BeforeEach
    public void setup() {
        transactionMock = Transaction.builder()
                .billedAmount(BigDecimal.valueOf(99.5))
                .amount(BigDecimal.valueOf(100))
                .transactionFee(BigDecimal.valueOf(0.5))
                .commission(BigDecimal.valueOf(0.001))
                .transactionReference(transactionRef)
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

        transactionRequestMock = new TransferRequest(
                transactionRef,
                BigDecimal.valueOf(1000),
                "Test Fund Transfer",
                originatorAccountNumber1,
                originatorAccountName1,
                "044",
                "GTBank",
                beneficiaryAccountNumber1,
                beneficiaryAccountName1,
                "044",
                "GTBank"
        );

        transactionListMock.add(transactionMock);
        transactionPageMock = new PageImpl<>(transactionListMock, Pageable.unpaged(), 1l);
    }
    @Test
    void transferFunds() {
        when(transactionRepo.findFirstByTransactionReference(transactionRef)).thenReturn(Optional.empty());
        when(paymentProcessorService.processTransfer(transactionRequestMock)).thenReturn(Status.SUCCESSFUL);
//        assertNotNull(transactionService.transferFunds(transactionRequestMock));
        assertDoesNotThrow(() -> transactionService.transferFunds(transactionRequestMock));
    }

    @Test
    void processSuccessfulTransactions() {
        when(transactionRepo.findAllByStatusAndHasBeenProcessed(Status.SUCCESSFUL, false)).thenReturn(transactionListMock);
        when(paymentProcessorService.processTransfer(transactionRequestMock)).thenReturn(Status.SUCCESSFUL);
        assertDoesNotThrow(() -> transactionService.processSuccessfulTransactions());
    }

    @Test
    void searchTransactions() {
        when(transactionRepo.searchTransactions(transactionRef, null, null, null, null, null, Pageable.unpaged())).thenReturn(transactionPageMock);
        assertDoesNotThrow(() -> transactionService.searchTransactions(transactionRef, null, null, null, null, null, Pageable.unpaged()));
    }
}