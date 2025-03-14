package Development.Rodrigues.Almeidas_Cortes.order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.commons.services.MapConverterService;
import Development.Rodrigues.Almeidas_Cortes.materials.MaterialRepository;
import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;
import Development.Rodrigues.Almeidas_Cortes.order.dto.CreateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.FilterDateOrdersDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.UpdateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.order.entities.ListOrder;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;

@Service
public class OrderService {

    @Autowired
    OrderRepository repository;

    @Autowired
    MaterialRepository materialRepository;

    public ResponseDTO getOrderByIdService(Long id) {
        Optional<Order> exists = repository.findById(id);

        if(exists.isPresent()) return new ResponseDTO(exists, "", "", "");

        return new ResponseDTO("", "Dados informados incorretos!", "", "");
    }

    public ResponseDTO getAllOrdersDateService(FilterDateOrdersDTO dados) {
        LocalDateTime dateInicio = dados.date().withHour(00).withMinute(00).withSecond(00).withNano(000000);
        LocalDateTime dateFim = dados.date().withHour(23).withMinute(59).withSecond(59).withNano(999999);

        List<Order> list = repository.findByDataPedidoBetween(dateInicio, dateFim);
    
        if(!list.isEmpty()) {
            List<ListOrder> listFormatted = new ArrayList<ListOrder>();

            List<Material> materials = materialRepository.findAll();

            for (Order item : list) {
                Object listGrade = MapConverterService.ConvertStringToObject(item.getGrade());
                List<String> metragemRecebido = item.getMetragemRecebido() != null 
                    ? new ArrayList<>(Arrays.asList(item.getMetragemRecebido().split(",\\s*"))) 
                    : new ArrayList<>();
                List<String> metragemFinalizado = item.getMetragemFinalizado() != null 
                    ? new ArrayList<>(Arrays.asList(item.getMetragemFinalizado().split(",\\s*"))) 
                    : new ArrayList<>();
                List<String> rendimentoList = item.getRendimentoParesMetro() != null 
                    ? new ArrayList<>(Arrays.asList(item.getRendimentoParesMetro().split(",\\s*"))) 
                    : new ArrayList<>();
                List<String> corList = item.getCor() != null 
                    ? new ArrayList<>(Arrays.asList(item.getCor().split(",\\s*"))) 
                    : new ArrayList<>();

                List<Long> idList = Arrays.stream(item.getTipoRecebido().split(",\\s*"))
                    .map(String::trim)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());

                List<Object> materialList = materials.stream()
                    .filter(material -> idList.contains(material.getId()))
                    .collect(Collectors.toList());
                
                ListOrder newItem = new ListOrder(
                    item.getId(),
                    item.getClient(),
                    item.getModelo(),
                    item.getDataPedido(),
                    item.getDataFinalizado(),
                    item.getRelatorioCliente(),
                    item.getTotalDinheiro(),
                    item.getTotalPares(),
                    listGrade,item.getObs(),
                    item.getDataPagamento(),
                    metragemRecebido,
                    materialList,
                    metragemFinalizado,
                    rendimentoList,
                    corList
                );

                listFormatted.add(newItem);
            };

            return new ResponseDTO(listFormatted,"","","");
        }

        return new ResponseDTO("", "", "", "Não existem pedidos para esta data!");
    }

    public ResponseDTO createOrderService(CreateOrderDTO dados) {
        Order newOrder = new Order(dados);
        
        repository.save(newOrder);

        return new ResponseDTO("", "", "Pedido Registrado com sucesso!", "");
    }
    
    public ResponseDTO updateOrderService(UpdateOrderDTO dados) {
        Optional<Order> exists = repository.findById(dados.id());

        if(exists.isPresent()) {
            Order order = exists.get();

            order.updateOrder(dados);

            repository.save(order);
            return new ResponseDTO("", "", "Pedido alterado com sucesso!", "");
        }

        return new ResponseDTO("", "Dados informados incorretos!", "", "");
    }
    
}
