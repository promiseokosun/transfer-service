package com.dot.ai.transferservice.apiresponse;

import com.dot.ai.transferservice.constant.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.ConstraintViolation;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
  @JsonIgnore
  private HttpStatus httpStatus;
  private String message;
  private String error;
  private String debugMessage;
  private List<ApiSubError> subErrors;
  private T data;
  private String payload;
  private String responseCode;
  private ResponseStatus responseStatus;

  public ApiResponse(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  private void addSubError(ApiSubError subError) {
    if (subErrors == null) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(subError);
  }

  private void addValidationError(String object, String field, Object rejectedValue, String message) {
    addSubError(new ApiValidationError(object, field, rejectedValue, message));
  }

  private void addValidationError(String object, String message) {
    addSubError(new ApiValidationError(object, message));
  }

  private void addValidationError(FieldError fieldError) {
    this.addValidationError(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(),
        fieldError.getDefaultMessage());
  }

  public void addValidationErrors(List<FieldError> fieldErrors) {
    fieldErrors.forEach(this::addValidationError);
  }

  private void addValidationError(ObjectError objectError) {
    this.addValidationError(objectError.getObjectName(), objectError.getDefaultMessage());
  }

  public void addValidationError(List<ObjectError> globalErrors) {
    globalErrors.forEach(this::addValidationError);
  }

  private void addValidationError(ConstraintViolation<?> cv) {
    this.addValidationError(cv.getRootBeanClass().getSimpleName(),
        ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(), cv.getInvalidValue(), cv.getMessage());
  }

  public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
    constraintViolations.forEach(this::addValidationError);
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public void setHttpStatus(HttpStatus httpStatus) {
    this.httpStatus = httpStatus;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getDebugMessage() {
    return debugMessage;
  }

  public void setDebugMessage(String debugMessage) {
    this.debugMessage = debugMessage;
  }

  public List<ApiSubError> getSubErrors() {
    return subErrors;
  }

  public void setSubErrors(List<ApiSubError> subErrors) {
    this.subErrors = subErrors;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  public String getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(String responseCode) {
    this.responseCode = responseCode;
  }

  public ResponseStatus getResponseStatus() {
    return responseStatus;
  }

  public void setResponseStatus(ResponseStatus responseStatus) {
    this.responseStatus = responseStatus;
  }

}