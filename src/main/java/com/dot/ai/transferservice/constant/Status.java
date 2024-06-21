package com.dot.ai.transferservice.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {
    SUCCESSFUL("Request Successful"), INSUFFICIENT_FUND("Insufficient fund"), FAILED("Error Processing Request");
    private String message;
}
