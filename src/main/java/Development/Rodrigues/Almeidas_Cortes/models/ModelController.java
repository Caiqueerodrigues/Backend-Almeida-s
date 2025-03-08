package Development.Rodrigues.Almeidas_Cortes.models;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.anexos.AnexosService;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.models.dto.CreateModelDTO;
import Development.Rodrigues.Almeidas_Cortes.models.dto.UpdateModelDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/models")
public class ModelController {

    @Autowired
    ModelService service;

    @GetMapping("/client/{idClient}")
    @Operation(summary = "Retorna todos os modelos do cliente")
    public ResponseEntity getAllModelsClientService(@PathVariable @Valid Long idClient) {
        try {
            ResponseDTO response = service.getAllModelsClientService(idClient);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @PostMapping()
    @Transactional
    @Operation(summary = "Cria um modelo para o cliente")
    public ResponseEntity createModelClient(@RequestBody @Valid CreateModelDTO dados) {
        try {
            ResponseDTO response = service.createModelClient(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @GetMapping("/{idModel}")
    @Operation(summary = "Retorna os dados de um modelo específfico")
    public ResponseEntity getModelId(@PathVariable @Valid Long idModel) {
        try {
            ResponseDTO response = service.getModelIdService(idModel);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
    

    @PutMapping()
    @Transactional
    @Operation(summary = "Altera os dados de um determinado modelo")
    public ResponseEntity updateModelClient(@RequestBody @Valid UpdateModelDTO dados) {
        try {
            ResponseDTO response = service.updateModelService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!" + e, "", ""));
        }
    }
}
