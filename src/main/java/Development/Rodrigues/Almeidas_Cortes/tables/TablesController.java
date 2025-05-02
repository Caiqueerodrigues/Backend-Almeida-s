package Development.Rodrigues.Almeidas_Cortes.tables;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/tables")
public class TablesController {
    @Autowired
    private TablesService tablesService;

    @GetMapping("/{idClient}")
    public ResponseEntity<ResponseDTO> getTablesClient(@PathVariable @Valid Long idClient) {
        try {
            ResponseDTO response = tablesService.getTablesClient(idClient);

            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
    

}
