package Development.Rodrigues.Almeidas_Cortes.materials;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.CreateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.UpdateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;




@RestController
@RequestMapping("/materials")
public class MaterialController {

    @Autowired
    MaterialService materialService;

    @GetMapping()
    @Operation(summary = "Obtém materiais")
    public ResponseEntity<ResponseDTO> getMaterials() {
        try {
            ResponseDTO response = materialService.getMaterialsService();

            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @GetMapping("/{idMaterial}")
    @Operation(summary = "Obtém materiais")
    public ResponseEntity<ResponseDTO> getMaterialId(@PathVariable @Valid Long idMaterial) {
        try {
            ResponseDTO response = materialService.getMateriaIdService(idMaterial);

            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @GetMapping("/active")
    @Operation(summary = "Obtém materiais ativos")
    public ResponseEntity<ResponseDTO> getMaterialsActive() {
        try {
            ResponseDTO response = materialService.getMaterialsActiveService();

            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
    
    @PostMapping
    @Transactional
    @Operation(summary = "Cadastra um novo material")
    public ResponseEntity<ResponseDTO> createMaterial(@RequestBody @Valid CreateMaterialDTO dados) {
        try {
            ResponseDTO response = materialService.createMaterialService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Altera informações do material selecionado")
    public ResponseEntity<ResponseDTO> putMaterial(@RequestBody @Valid UpdateMaterialDTO dados) {
        try {
            ResponseDTO response = materialService.putMaterialService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

}
