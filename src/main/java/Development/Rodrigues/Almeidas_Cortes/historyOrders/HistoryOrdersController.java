package Development.Rodrigues.Almeidas_Cortes.historyOrders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.FilterDateOrdersDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/history-orders")
public class HistoryOrdersController {

    @Autowired
    HistoryOrderService service;

    @PostMapping
    @Operation(summary = "Retorna todos os históricos dos pedidos")
    public ResponseEntity GetOrderId(@RequestBody FilterDateOrdersDTO dados) {
        try {
            ResponseDTO response = service.getHistoryOrdersDateService(dados.date());
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", e.getMessage(), "", ""));
        }
    }
}
