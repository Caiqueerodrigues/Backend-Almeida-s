package Development.Rodrigues.Almeidas_Cortes.employees;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.employees.dto.CreateEmployeeDTO;
import Development.Rodrigues.Almeidas_Cortes.employees.dto.UpdateEmployeeDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @GetMapping("/{dateInicial}/{dateFinal}")
    @Operation(summary = "Buscas todos os registros da data fornecida")
    public ResponseEntity getAllDate(@PathVariable LocalDate dateInicial, @PathVariable LocalDate dateFinal) {
        try {
            ResponseDTO resp = employeeService.getAllDateService(dateInicial, dateFinal);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }

    @GetMapping("id/{id}")
    @Operation(summary = "Buscas todos os registros da data fornecida")
    public ResponseEntity getId(@PathVariable Long id) {
        try {
            ResponseDTO resp = employeeService.getIdService(id);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }

    @PostMapping
    @Transactional
    @Operation(summary = "Insere um novo reggistro")
    public ResponseEntity createEmployee(@RequestBody @Valid CreateEmployeeDTO createEmployeeDTO) {
        try {
            ResponseDTO resp = employeeService.createRegisterService(createEmployeeDTO);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Altera um reggistro")
    public ResponseEntity updateEmployee(@RequestBody @Valid UpdateEmployeeDTO update) {
        try {
            ResponseDTO resp = employeeService.updateRegisterService(update);
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }

    @PutMapping("/update-status-payment")
    @Transactional
    @Operation(summary = "Altera um Status por lote")
    public ResponseEntity updateStatusPayment(@RequestBody List<Long> ids) {
        try {
            ResponseDTO response = employeeService.updateStatusPaymentService(ids);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }
}
