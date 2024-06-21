package com.dot.ai.transferservice.controller;


import com.dot.ai.transferservice.apiresponse.ApiResponse;
import com.dot.ai.transferservice.dto.request.TransferRequest;
import com.dot.ai.transferservice.dto.response.TransactionResponse;
import com.dot.ai.transferservice.dto.response.TransactionSummaryResponse;
import com.dot.ai.transferservice.model.projections.TransactionSummaryProj;
import com.dot.ai.transferservice.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Objects;

import static com.dot.ai.transferservice.constant.ApiConstants.LOGGER_STRING_GET;
import static com.dot.ai.transferservice.constant.ApiConstants.LOGGER_STRING_POST;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    @GetMapping("/search")
    public ResponseEntity<?> searchTransactions(@RequestParam(value = "transactionReference", required = false, defaultValue = "") String transactionReference,
                                                @RequestParam(value = "originatorAccountNumber", required = false, defaultValue = "") String originatorAccountNumber,
                                                @RequestParam(value = "beneficiaryAccountNumber", required = false, defaultValue = "") String beneficiaryAccountNumber,
                                                @RequestParam(value = "status", required = false, defaultValue = "") String status,
                                                @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
                                                @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate,
                                                Pageable pageable,
                                                HttpServletRequest servletRequest) {
        String url = servletRequest.getRequestURL().toString();
        Page<TransactionResponse> transactions = transactionService.searchTransactions(
                StringUtils.isBlank(transactionReference) ? null : transactionReference,
                StringUtils.isBlank(originatorAccountNumber) ? null : originatorAccountNumber,
                StringUtils.isBlank(beneficiaryAccountNumber) ? null : beneficiaryAccountNumber,
                StringUtils.isBlank(status) ? null : status,
                StringUtils.isBlank(startDate) ? null : LocalDate.parse(startDate),
                StringUtils.isBlank(endDate) ? null : LocalDate.parse(endDate),
                pageable
        );
        ApiResponse<Page<TransactionResponse>> apiResponse = new ApiResponse<>(HttpStatus.OK);
        apiResponse.setData(transactions);
        apiResponse.setResponseCode("00");
        apiResponse.setMessage("Request Successful");
        log.info(LOGGER_STRING_GET, url, apiResponse);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getTransactionSummary(@RequestParam(value = "searchDate") String searchDate,
                                                   HttpServletRequest servletRequest) {
        String url = servletRequest.getRequestURL().toString();
        TransactionSummaryProj transactionSummaryProj =
                transactionService.getTransactionSummary(LocalDate.parse(searchDate));
        ApiResponse<TransactionSummaryProj> apiResponse = new ApiResponse<>(HttpStatus.OK);
        apiResponse.setResponseCode("00");
        apiResponse.setData(transactionSummaryProj);
        apiResponse.setMessage("Request Successful");
        log.info(LOGGER_STRING_GET, url, apiResponse);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

    @GetMapping("/summary2")
    public ResponseEntity<?> getTransactionSummary2(@RequestParam(value = "searchDate") String searchDate,
                                                   HttpServletRequest servletRequest) {
        String url = servletRequest.getRequestURL().toString();

        TransactionSummaryResponse transactionSummary = transactionService.getTransactionSummaryV2(LocalDate.parse(searchDate));
        ApiResponse<TransactionSummaryResponse> apiResponse = new ApiResponse<>(HttpStatus.OK);
        apiResponse.setResponseCode("00");
        apiResponse.setData(transactionSummary);
        apiResponse.setMessage("Request Successful");
        log.info(LOGGER_STRING_GET, url, apiResponse);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }


    @PostMapping("/transfer-fund")
    public ResponseEntity<?> transferFund(@Valid @RequestBody TransferRequest request,
                                          HttpServletRequest servletRequest) {
        String url = servletRequest.getRequestURL().toString();
        TransactionResponse transactionResponse = transactionService.transferFunds(request);
        ApiResponse<TransactionResponse> apiResponse = new ApiResponse<>(HttpStatus.OK);
        apiResponse.setResponseCode("00");
        apiResponse.setData(transactionResponse);
        apiResponse.setMessage("Request Successful");
        log.info(LOGGER_STRING_POST, url, request, apiResponse);
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }

}
