package com.dot.ai.transferservice.exception;

import com.dot.ai.transferservice.apiresponse.ApiResponse;
import com.dot.ai.transferservice.constant.ResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.DateTimeException;
import java.time.format.DateTimeParseException;

import static com.dot.ai.transferservice.constant.ApiConstants.LOGGER_STRING_GET;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException ce) {
        log.info(" in handleCustomException ",ce);
        ApiResponse<?> ar = new ApiResponse<>(ce.getStatus());
        ar.setResponseCode("07");
        ar.setResponseStatus(ResponseStatus.FAILED);
        if (ce.getStatus().is2xxSuccessful()) {
            ar.setResponseCode("00");
            ar.setMessage(ce.getMessage());
        } else {
            ar.setError(ce.getMessage());
        }
        return buildResponseEntity(ar);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleException(DataIntegrityViolationException ce) {
        log.error("Error Message -> ", ce);
        ApiResponse<?> ar = new ApiResponse<>(HttpStatus.CONFLICT);
        ar.setResponseCode("09");
        ar.setResponseStatus(ResponseStatus.FAILED);
        ar.setError(ce.getLocalizedMessage().substring(ce.getMessage().indexOf('[')+1,ce.getMessage().indexOf(']')));
        return buildResponseEntity(ar);
    }

    @ExceptionHandler(DateTimeException.class)
    public ResponseEntity<Object> handleDateTimeException(DateTimeException ce) {
        log.error("in handleDateTimeException -> ", ce);
        ApiResponse<?> ar = new ApiResponse<>(HttpStatus.BAD_REQUEST);
        ar.setResponseCode("05");
        ar.setResponseStatus(ResponseStatus.FAILED);
        ar.setError("Invalid date");
        return buildResponseEntity(ar);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<Object> handleDateTimeParseException(DateTimeParseException ce) {
        log.error("In handleDateTimeParseException -> ", ce);
        ApiResponse<?> ar = new ApiResponse<>(HttpStatus.BAD_REQUEST);
        ar.setResponseCode("04");
        ar.setResponseStatus(ResponseStatus.FAILED);
        ar.setError("Invalid date format");
        return buildResponseEntity(ar);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {

        HttpServletRequest servletRequest = ((ServletWebRequest)request).getRequest();
        String url = servletRequest.getRequestURL().toString();
        log.error("In handleMethodArgumentNotValid -> ", ex);
        ApiResponse<?> ar = new ApiResponse<>(HttpStatus.BAD_REQUEST);
        ar.setResponseCode("03");
        ar.setResponseStatus(ResponseStatus.FAILED);
        ar.addValidationError(ex.getBindingResult().getAllErrors());
        ar.setError("Validation Error");
        ResponseEntity<Object> response = buildResponseEntity(ar);
        log.info(LOGGER_STRING_GET, url,response );
        return response;
    }

    private ResponseEntity<Object> buildResponseEntity(ApiResponse<?> apiResponse) {
        return new ResponseEntity<>(apiResponse, apiResponse.getHttpStatus());
    }
}
