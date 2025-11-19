package com.cognizant.demo.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<String> recordNotFoundExceptionInfo(NoDataFoundException e) {
		String strMsg = e.getMessage();
		return new ResponseEntity<>(strMsg, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<String> recordAlreadyExistsExceptionInfo(UserAlreadyExistsException e){
		String strMsg = e.getMessage();
		return new ResponseEntity<>(strMsg,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(NoUserFoundException.class)
	public ResponseEntity<String> noUserFoundExceptionInfo(NoUserFoundException e){
		String strMsg = e.getMessage();
		return new ResponseEntity<>(strMsg,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidCredentailsException.class)
	public ResponseEntity<String> invalidCredentailsExceptionInfo(InvalidCredentailsException e){
		String strMsg = e.getMessage();
		return new ResponseEntity<>(strMsg,HttpStatus.NOT_ACCEPTABLE);
	}
	@ExceptionHandler(DataAlreadyFoundExcecption.class)
	 public ResponseEntity<String> dataAlreadyFoundExcecptioninfo(DataAlreadyFoundExcecption e) {
       String strMsg = e.getMessage();
       return new ResponseEntity<>(strMsg, HttpStatus.CONFLICT);
   }

    @ExceptionHandler(TicketCreationException.class)
    public ResponseEntity<String> handleTicketCreationException(TicketCreationException ex) {
    	 String strMsg = ex.getMessage();
         return new ResponseEntity<>(strMsg, HttpStatus.CONFLICT);
    }
    
    public ResponseEntity<String> ticketNotFoundExceptioninfo(TicketNotFoundException ex){
    	 String strMsg = ex.getMessage();
         return new ResponseEntity<>(strMsg, HttpStatus.CONFLICT);
    }

   
    @ExceptionHandler(RecordAlreadyFoundException.class)
    public ResponseEntity<String> handleDuplicate(RecordAlreadyFoundException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }

    @ExceptionHandler(PdfGenerationException.class)
    public ResponseEntity<byte[]> handlePdfError(PdfGenerationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage().getBytes());
    }
	@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneric(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
    }

}
