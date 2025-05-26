package Development.Rodrigues.Almeidas_Cortes.order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import Development.Rodrigues.Almeidas_Cortes.*;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.commons.services.MapConverterService;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.HistoryOrderService;
import Development.Rodrigues.Almeidas_Cortes.materials.MaterialRepository;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.CreateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;
import Development.Rodrigues.Almeidas_Cortes.order.dto.CreateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.FilterDateOrdersDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.UpdateOrderDTO;
import Development.Rodrigues.Almeidas_Cortes.order.dto.UpdatePaymentDTO;
import Development.Rodrigues.Almeidas_Cortes.order.entities.ListOrder;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import Development.Rodrigues.Almeidas_Cortes.order.entities.OrderFront;
import Development.Rodrigues.Almeidas_Cortes.report.dto.ParamsFiltersReports;
import Development.Rodrigues.Almeidas_Cortes.users.UserRepository;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;
import jakarta.servlet.http.HttpServletRequest;
@Service
public class OrderService {

    @Autowired
    OrderRepository repository;
    
    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    HistoryOrderService historyOrderService;

    @Autowired
    UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(HistoryOrderService.class);

    public ResponseDTO getOrderByIdService(Long id) {
        try {
            Optional<Order> exists = repository.findById(id);
    
            if(exists.isPresent()) {
                OrderFront order = new OrderFront(
                    exists.get().getClient(),
                    exists.get().getModelo(),
                    exists.get().getDataPedido(),
                    exists.get().getDataFinalizado(),
                    exists.get().getRelatorioCliente(),
                    exists.get().getTotalDinheiro(),
                    exists.get().getTotalPares(),
                    exists.get().getGrade(),
                    exists.get().getObs(),
                    exists.get().getDataPagamento(),
                    exists.get().getTipoRecebido(),
                    exists.get().getRendimentoParesMetro(),
                    exists.get().getQuemAssinou(),
                    exists.get().getCor(),
                    exists.get().getDataRetirada(),
                    exists.get().getQuemCortou()
                );
                return new ResponseDTO(order, "", "", "");
            }

            return new ResponseDTO("", "Dados informados incorretos!", "", "");
        } catch (Exception e) {
            log.error("ERRO ao obter o pedido por id " + e);
            throw new RuntimeException("Erro ao obter o pedido, tente novamente.");
        }
    }

    public ResponseDTO getAllOrdersDateService(FilterDateOrdersDTO dados) {
        try {
            
            LocalDateTime dateInicio = dados.date().withHour(00).withMinute(00).withSecond(00).withNano(000000);
            LocalDateTime dateFim = dados.date().withHour(23).withMinute(59).withSecond(59).withNano(999999);
    
            List<Order> list = repository.findByDataPedidoBetween(dateInicio, dateFim);
        
            if(!list.isEmpty()) {
                List<ListOrder> listFormatted = createListOrder(list);
    
                return new ResponseDTO(listFormatted,"","","");
            }
    
            return new ResponseDTO("", "", "", "Não existem pedidos para esta data!");
        } catch (Exception e) {
            log.error("ERRO ao obter os pedidos pela data " + e);
            throw new RuntimeException("Erro ao buscas os pedidos, tente novamente.");
        }
    }

    public ResponseDTO createOrderService(CreateOrderDTO dados) {
        try {
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            
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
            
            Order newOrder = new Order(dados, user);
            if(novosMateriais.length() > 0) {
                if (novosMateriais.endsWith(", ")) {
                    novosMateriais = novosMateriais.substring(0, novosMateriais.length() - 2);
                }
                newOrder.setTipoRecebido(novosMateriais);
            }
            
            repository.save(newOrder);
    
            historyOrderService.createHistory(newOrder, "Pedido cadastrado", user);
    
            return new ResponseDTO("", "", "Pedido Registrado com sucesso!", "");
        } catch (Exception e) {
            log.error("ERRO ao cadastrar o pedido " + e);
            throw new RuntimeException("Erro ao cadastrar o pedido, tente novamente.");
        }
    }
    
    public ResponseDTO updateOrderService(UpdateOrderDTO dados) {
        try {
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
    
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
    
                historyOrderService.createHistory(order, "Alteração de pedido", user);
    
                repository.save(order);
                return new ResponseDTO("", "", "Pedido alterado com sucesso!", "");
            }
    
            return new ResponseDTO("", "Dados informados incorretos!", "", "");
        } catch (Exception e) {
            log.error("ERRO ao alterar o pedido " + e);
            throw new RuntimeException("Erro ao alterar o pedido, tente novamente.");
        }
    }

    public ResponseDTO getOrderPeriodService(String initialDateStr, String finalDateStr) {
        try {
            LocalDateTime dateInicio = stringToLocalDatetime("initial", initialDateStr);
            LocalDateTime dateFim = stringToLocalDatetime("final", finalDateStr);
    
            List<Order> list = repository.findByDataPedidoBetween(dateInicio, dateFim);
    
            if(!list.isEmpty()) {
                List<ListOrder> listFormatted = createListOrder(list);
    
                return new ResponseDTO(listFormatted,"","","");
            }
    
            return new ResponseDTO("", "", "", "Não existem dados para estas datas");
        } catch (Exception e) {
            log.error("ERRO ao obter os pedido por periodo " + e);
            throw new RuntimeException("Erro ao obter os pedidos, tente novamente.");
        }

    }

    public ResponseDTO updateDatePaymentService(UpdatePaymentDTO dados) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
    
            List<Order> orders = repository.findAllById(dados.ids());
    
            if(orders.size() > 0) {
                orders.forEach(order -> {
                    order.setDataPagamento(dados.date());
                    repository.save(order);
    
                    historyOrderService.createHistory(order, "Registrando pagamento", user);
                });
    
                return new ResponseDTO("", "", "Dados salvos com sucesso!", "");
            }
            return new ResponseDTO("", "", "", "Dados informados incorretos!");
        } catch (Exception e) {
            log.error("ERRO ao registar o pagamento do pedido " + e);
            throw new RuntimeException("Erro ao registar o pagamento, tente novamente.");
        }
    }
    
    public ResponseDTO getOrdersDueService() {
        try {
            List<Order> orders = repository.findByDataPagamentoIsNull();
    
            if(orders.size() > 0) {
                List<ListOrder> listFormatted = createListOrder(orders);
                return new ResponseDTO(listFormatted, "", "", "");
            }

            return new ResponseDTO("", "", "", "Não existem pedidos pendentes!");
        } catch (Exception e) {
            log.error("ERRO ao obter os pedidos pendentes " + e);
            throw new RuntimeException("Erro ao obter os pedidos pendentes, tente novamente.");
        }
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
                corList,
                item.getQuemAssinou(),
                item.getQuemCortou(),
                item.getDataPagamento() != null ? "Sim" : "Não"
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
