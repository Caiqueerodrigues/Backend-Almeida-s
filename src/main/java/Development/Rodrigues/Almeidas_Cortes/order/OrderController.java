package Development.Rodrigues.Almeidas_Cortes.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.CreateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.FilterDateOrdersDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.UpdateOrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService service;


    @GetMapping("/{idOrder}")
    @Operation(summary = "Retorna todos os pedidos registrados naquela data")
    public ResponseEntity GetOrderId(@PathVariable @Valid Long idOrder) {
        try {
            ResponseDTO response = service.getOrderByIdService(idOrder);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }

    @PostMapping("/date")
    @Transactional
    @Operation(summary = "Retorna todos os pedidos registrados naquela data")
    public ResponseEntity getOrdersDate(@RequestBody @Valid FilterDateOrdersDTO dados) {
        try {
            ResponseDTO response = service.getAllOrdersDateService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!" + e, "", ""));
        }
    }

    @PutMapping
    @Transactional
    @Operation(summary = "Altera os dados de um determinado pedido")
    public ResponseEntity updateOrder(@RequestBody @Valid UpdateOrderDTO dados) {
        try {
            ResponseDTO response = service.updateOrderService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
    
    @PostMapping("/create-order")
    @Operation(summary = "Registra um novo pedido")
    public ResponseEntity createOrder(@RequestBody @Valid CreateOrderDTO dados) {
        try {
            ResponseDTO response = service.createOrderService(dados);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
    

    @GetMapping("/period/{initialDate}/{finalDate}")
    public ResponseEntity getOrderPeriod(@PathVariable String initialDate, @PathVariable String finalDate) {
        try {
            ResponseDTO response = service.getOrderPeriodService(initialDate, finalDate);
            return ResponseEntity.status(200).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("", "Desculpe, tente novamente mais tarde!", "", ""));
        }
    }
}
