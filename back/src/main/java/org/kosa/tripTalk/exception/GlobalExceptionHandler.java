package org.kosa.tripTalk.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public  ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
							.body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationEx(MethodArgumentNotValidException ex) {
		String message = ex.getBindingResult().getFieldError().getDefaultMessage();
		return ResponseEntity
							.status(HttpStatus.BAD_REQUEST)
							.body(new ErrorResponse("INVALID_INPUT", message));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleEx(Exception ex) {
		ex.printStackTrace();
		return ResponseEntity
							.status(HttpStatus.INTERNAL_SERVER_ERROR)
							.body(new ErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage()));
	}
}
