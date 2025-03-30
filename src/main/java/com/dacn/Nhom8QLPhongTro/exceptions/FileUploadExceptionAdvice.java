package com.dacn.Nhom8QLPhongTro.exceptions;


import com.dacn.Nhom8QLPhongTro.message.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.setMessage("Tệp tải lên vượt mức quy định!" + exc.getMessage());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
    }

}
