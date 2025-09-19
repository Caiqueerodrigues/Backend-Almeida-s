package Development.Rodrigues.Almeidas_Cortes.exit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.DeleteExchange;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.dto.CreateExitDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.dto.SearchDateDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.dto.UpdateExitDTO;
import jakarta.validation.Valid;



@RestController
@RequestMapping("/exit")
public class ExitController {
    @Autowired
    ExitService service;

    @PostMapping
    public ResponseEntity getExistsDayController(@RequestBody SearchDateDTO dados) {
        try {
            ResponseDTO response = service.getExistsDayService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity getExitByIdController(@PathVariable @Valid Long id) {
        try {
            ResponseDTO response = service.getExitByIdService(id);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }

    @PostMapping("/create")
    public ResponseEntity createExistsDayController(@RequestBody CreateExitDTO dados) {
        try {
            ResponseDTO response = service.createExitService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }

    @PutMapping
    public ResponseEntity updateExistController(@RequestBody UpdateExitDTO dados) {
        try {
            ResponseDTO response = service.updateExitService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }

    @DeleteExchange("/{id}")
    public ResponseEntity deleteExistController(@PathVariable @Valid Long id) {
        try {
            ResponseDTO response = service.deleteExitService(id);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }
}
