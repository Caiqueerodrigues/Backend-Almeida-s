package Development.Rodrigues.Almeidas_Cortes.finance;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/finance")
public class FinanceController {

    @Autowired
    FinanceService service;

    @GetMapping("/{initialDate}/{finalDate}")
    @Operation(summary = "Retorna todos os dados financeiros do período")
    public ResponseEntity GetFinanceController(@PathVariable @Valid LocalDate initialDate, @PathVariable @Valid LocalDate finalDate) {
        try {
            ResponseDTO response = service.getFinanceDataService(initialDate, finalDate);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }
    
}
