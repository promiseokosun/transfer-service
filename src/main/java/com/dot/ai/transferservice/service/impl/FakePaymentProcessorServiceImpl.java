package com.dot.ai.transferservice.service.impl;

import com.dot.ai.transferservice.constant.Status;
import com.dot.ai.transferservice.dto.request.TransferRequest;
import com.dot.ai.transferservice.service.PaymentProcessorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class FakePaymentProcessorServiceImpl implements PaymentProcessorService {
    @Override
    public Status processTransfer(TransferRequest request) {
        // call processors
        Status [] statuses = {Status.SUCCESSFUL, Status.FAILED, Status.INSUFFICIENT_FUND};
        Random random = new Random();
        return statuses[random.nextInt(statuses.length)];
    }
}
