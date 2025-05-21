package Development.Rodrigues.Almeidas_Cortes.configs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleAllExceptions(Exception ex) {
        return ResponseEntity.status(500)
                .body(new ResponseDTO("", ex.getMessage(), "", ""));
    }
}
