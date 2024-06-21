package com.dot.ai.transferservice.service;

import com.dot.ai.transferservice.constant.Status;
import com.dot.ai.transferservice.dto.request.TransferRequest;

public interface PaymentProcessorService {
    Status processTransfer(TransferRequest request);
}
