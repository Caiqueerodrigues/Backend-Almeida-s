package Development.Rodrigues.Almeidas_Cortes.clients;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.clients.dto.CreateClientDTO;
import Development.Rodrigues.Almeidas_Cortes.clients.dto.UpdateClientDTO;
import Development.Rodrigues.Almeidas_Cortes.clients.dto.UpdateClientDTO;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired 
    ClientService service;

    @GetMapping()
    @Operation(summary = "Retorna todos os clientes")
    public ResponseEntity getAllClients() {
        try {
            ResponseDTO response = service.getAllClientsService();
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @GetMapping("/active")
    @Operation(summary = "Retorna todos os clientes que estiverem ativos")
    public ResponseEntity getAllClientsActive() {
        try {
            ResponseDTO response = service.getAllClientsActiveService();
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Retorna os dados de um cliente especifico")
    public ResponseEntity getClientId(@PathVariable Long id) {
        try {
            ResponseDTO response = service.getClientIdService(id);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
    

    @PostMapping
    @Transactional
    @Operation(summary = "Cadastra um novo cliente")
    public ResponseEntity createClient(@RequestBody @Valid CreateClientDTO dados) {
        try {
            ResponseDTO response = service.createClientService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
    
    @PutMapping()
    @Transactional    
    @Operation(summary = "Altera os dados de um cliente especifico")
    public ResponseEntity updateClient(@RequestBody @Valid UpdateClientDTO dados) {
        try {
            ResponseDTO response = service.updateClientService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
}
