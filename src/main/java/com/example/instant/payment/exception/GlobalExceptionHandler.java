package com.example.instant.payment.exception;

import com.example.instant.payment.constant.ErrorCode;
import com.example.instant.payment.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(Exception.class)
    public ResponseEntity handleException(HttpServletRequest req, Exception e){
        log.error(e.getMessage(), e);
        
        String errorMsg = (e.getMessage() == null) ? e.getClass().getSimpleName() : e.getMessage();
        Map<String,Object> error = Collections.singletonMap("error", errorMsg);
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
	
	@ExceptionHandler(BusinessException.class)
    public ResponseEntity handleBusinessException(HttpServletRequest req, Exception e){
		BusinessException businessEx = (BusinessException) e;
		
        String errorMsg = (e.getMessage() == null) ? e.getClass().getSimpleName() : e.getMessage();
        log.error("[" + businessEx.getErrorCode() + "] " + businessEx.getMessage());
        
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(businessEx.getErrorCode());
        response.setErrorMessage(errorMsg);
        
        return ResponseEntity.status(businessEx.getHttpStatus()).body(response);
    }
	
	@ExceptionHandler(value = { BindException.class, MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class })
	@ResponseBody
	public ResponseEntity handleValidationException(HttpServletRequest req, Exception e){
        log.error(e.getMessage());
        
        String errorMsg = (e.getMessage() == null) ? e.getClass().getSimpleName() : e.getMessage();
        
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode(ErrorCode.CLIENT_ERROR);
        response.setErrorMessage(errorMsg);
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
