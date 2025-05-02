package Development.Rodrigues.Almeidas_Cortes.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Development.Rodrigues.Almeidas_Cortes.*;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.commons.services.MapConverterService;
import Development.Rodrigues.Almeidas_Cortes.materials.MaterialRepository;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.CreateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;
import Development.Rodrigues.Almeidas_Cortes.order.dto.CreateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.FilterDateOrdersDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.UpdateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.order.entities.ListOrder;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import Development.Rodrigues.Almeidas_Cortes.report.dto.ParamsFiltersReports;

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
            List<ListOrder> listFormatted = createListOrder(list);

            return new ResponseDTO(listFormatted,"","","");
        }

        return new ResponseDTO("", "", "", "Não existem pedidos para esta data!");
    }

    public ResponseDTO createOrderService(CreateOrderDTO dados) {
        String novosMateriais = "";
        
        if(dados.tipoRecebido().matches(".*[a-zA-Z].*")) {
            List<String> materiais = Arrays.asList(dados.tipoRecebido().split(",\\s*"));
            
            for (String material : materiais) {
                if(material.matches(".*[a-zA-Z].*")) {
                    novosMateriais += consultMaterial(material);
                } else {
                    novosMateriais += material + ", ";
                }
            };
        }
        
        Order newOrder = new Order(dados);
        if(novosMateriais.length() > 0) {
            if (novosMateriais.endsWith(", ")) {
                novosMateriais = novosMateriais.substring(0, novosMateriais.length() - 2);
            }
            newOrder.setTipoRecebido(novosMateriais);
        }
        
        repository.save(newOrder);

        return new ResponseDTO("", "", "Pedido Registrado com sucesso!", "");
    }
    
    public ResponseDTO updateOrderService(UpdateOrderDTO dados) {
        String novosMateriais = "";

        if(dados.tipoRecebido().matches(".*[a-zA-Z].*")) {
            List<String> materiais = Arrays.asList(dados.tipoRecebido().split(",\\s*"));
            
            for (String material : materiais) {
                if(material.matches(".*[a-zA-Z].*")) {
                    novosMateriais += consultMaterial(material);
                } else {
                    novosMateriais += material + ", ";
                }
            };
        }

        Optional<Order> exists = repository.findById(dados.id());

        if(exists.isPresent()) {
            Order order = exists.get();

            order.updateOrder(dados);
                if(novosMateriais.length() > 0) {
                    if (novosMateriais.endsWith(", ")) {
                        novosMateriais = novosMateriais.substring(0, novosMateriais.length() - 2);
                    }
                    order.setTipoRecebido(novosMateriais);
                }

            repository.save(order);
            return new ResponseDTO("", "", "Pedido alterado com sucesso!", "");
        }

        return new ResponseDTO("", "Dados informados incorretos!", "", "");
    }

    public ResponseDTO getOrderPeriodService(String initialDateStr, String finalDateStr) {

        LocalDateTime dateInicio = stringToLocalDatetime("initial", initialDateStr);
        LocalDateTime dateFim = stringToLocalDatetime("final", finalDateStr);

        List<Order> list = repository.findByDataPedidoBetween(dateInicio, dateFim);

        if(!list.isEmpty()) {
            List<ListOrder> listFormatted = createListOrder(list);

            return new ResponseDTO(listFormatted,"","","");
        }

        return new ResponseDTO("", "", "", "Não existem dados para estas datas");
    }

    private List<ListOrder> createListOrder(List<Order> list) {
        List<ListOrder> listFormatted = new ArrayList<ListOrder>();

        List<Material> materials = materialRepository.findAll();

        for (Order item : list) {
            Object listGrade = MapConverterService.ConvertStringToObject(item.getGrade());
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
                materialList,
                rendimentoList,
                corList
            );

            listFormatted.add(newItem);
        }
        return listFormatted;
    }

    private LocalDateTime stringToLocalDatetime(String type, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        if(type.equals("initial")) {
            LocalDate initialDate = LocalDate.parse(date, formatter);
            
            return initialDate.atStartOfDay();
        }

        LocalDate finalDate = LocalDate.parse(date, formatter);
        return finalDate.atTime(23, 59, 59, 999999000);
    }

    private String consultMaterial(String nome) {
        Optional<Material> exists = materialRepository.findByNome(nome.trim());
        
        if(exists.isPresent()) {
            return exists.get().getId() + ", ";
        } else {
            Material material = new Material();
                material.setNome(nome.trim());
            Material salvo = materialRepository.save(material);
            return salvo.getId() + ", ";
        }
    }
}
