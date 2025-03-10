package Development.Rodrigues.Almeidas_Cortes.anexos;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import Development.Rodrigues.Almeidas_Cortes.anexos.dto.AnexoDTO;
import Development.Rodrigues.Almeidas_Cortes.anexos.dto.ListAnexosDTO;
import Development.Rodrigues.Almeidas_Cortes.anexos.dto.VariosAnexosDTO;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import org.springframework.http.MediaType;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@RestController
@RequestMapping("/anexo")
public class AnexoController {

    @Autowired
    AnexosService service;
    
    @PostMapping
    @Transactional
    @Operation(
        summary = "Anexa uma imagem a um determinado modelo",
        requestBody = @RequestBody( content = @Content( mediaType = "multipart/form-data" ) ) 
    )
    public ResponseEntity insertImageModel (
        @RequestParam(value = "id", required = false) Long id,
        @RequestParam(value = "files[]", required = false) List<MultipartFile> files,
        @RequestParam("nomePeca[]") List<String> nomePeca,
        @RequestParam("idModelo") Long idModelo,
        @RequestParam("idClient") Long idClient,
        @RequestParam("nomeModelo") String nomeModelo,
        @RequestParam("pecaPar[]") List<Long> pecaPar,
        @RequestParam("propriedadeFaca[]") List<String> propriedadeFaca,
        @RequestParam("precoFaca[]") List<Double> precoFaca,
        @RequestParam(value = "obs[]", required = false) List<String> obs,
        @RequestParam(value = "idsFotos[]", required = false) List<Long> idsFotos
    ) {
        try {
            List<ListAnexosDTO> anexos = new ArrayList<>();
            if(files != null && !files.isEmpty()) {
                for (int i = 0; i < files.size(); i++) {
                    nomePeca.set(i, nomePeca.get(i).replace(" ", "-"));
                    String nomeOriginalSemEspaco = files.get(i).getOriginalFilename().replace(" ", "-");
    
                    ListAnexosDTO anexo = new ListAnexosDTO(files.get(i), nomePeca.get(i), nomeOriginalSemEspaco);
                    anexos.add(anexo);
                }
            }

            VariosAnexosDTO dados = new VariosAnexosDTO(
                id, 
                anexos,
                idClient,
                nomePeca, 
                nomeModelo, 
                idModelo, 
                pecaPar, 
                propriedadeFaca, 
                precoFaca, 
                idsFotos,       
                obs
            );
            ResponseDTO response = service.insertImageModelService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!" +e, "", ""));
        }
    }

    @GetMapping("/{idClient}/{model}/{file}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String model, @PathVariable String file, @PathVariable String idClient) {
        try {
            Resource response = service.getPhotoService(model, file, idClient);
            String contentType = Files.probeContentType(Paths.get(response.getURI()));
            
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(response);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao tentar obter a imagem: " + file, e);
        }
    }
}
