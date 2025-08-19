package Development.Rodrigues.Almeidas_Cortes.historyOrders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.entities.HistoryOrders;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.entities.ListHistoryOrders;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import Development.Rodrigues.Almeidas_Cortes.users.UserRepository;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;

@Service
public class HistoryOrderService {
    @Autowired
    HistoryOrderRepository repository;

    @Autowired
    UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(HistoryOrderService.class);

    public void createHistory( Order pedido, String operation, User user ) {
        try {
            ZonedDateTime dateBrasilia = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
            LocalDateTime localDateTimeBrasilia = dateBrasilia.toLocalDateTime();
    
            HistoryOrders history = new HistoryOrders(pedido, operation, localDateTimeBrasilia, user);
    
            repository.save(history);
        } catch (Exception e) {
            log.error("ERRO na criação do histórico " + e);
            throw new RuntimeException("Erro ao inserir os dados no histórico, tente novamente.");
        }
    }

    public ResponseDTO getHistoryOrdersDateService(LocalDateTime date ) {
        try {
            
            LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
            LocalDateTime endOfDay = date.toLocalDate().atTime(23, 59, 59, 999_999_999);
    
            List<HistoryOrders> list = repository.findByUpdateAtBetweenOrderByIdDesc(startOfDay, endOfDay);
    
            List<ListHistoryOrders> listFinal= new ArrayList<ListHistoryOrders>();
    
            if (!list.isEmpty()) {
                list.forEach(item -> {
                    List<String> corList = item.getIdPedido().getCor() != null 
                        ? new ArrayList<>(Arrays.asList(item.getIdPedido().getCor().split(",\\s*"))) : 
                        new ArrayList<>();
    
                    listFinal.add(new ListHistoryOrders(
                        item.getId(),
                        item.getIdPedido().getId(), 
                        item.getIdPedido().getClient().getNome(),
                        item.getIdPedido().getTotalDinheiro(),
                        item.getIdPedido().getModelo().getTipo(),
                        corList,
                        item.getIdPedido().getModelo().getPreco(),
                        item.getUpdateAt(),
                        item.getIdUser().getName(),
                        item.getOperation()
                        )
                    );
                });
                return new ResponseDTO(listFinal, "", "", "");
            }
            return new ResponseDTO("", "", "", "Nenhum histórico encontrado para a data informada.");
        } catch (Exception e) {
            log.error("ERRO no get do histórico " + e);
            throw new RuntimeException("Erro ao obter os dados do histórico, tente novamente.");
        }
    }

    public List<HistoryOrders> getHistoryService(Order idPedido) {
        try {
            List<HistoryOrders> history = repository.findByIdPedido(idPedido);
            
            if (history.size() > 0) return history;
            else throw new RuntimeException("Histórico não encontrado para o ID: " + idPedido);
        } catch (Exception e) {
            log.error("ERRO no get do histórico " + e);
            throw new RuntimeException("Erro ao obter os dados do histórico, tente novamente.");
        }
    }

    public String deleteHistory(List<HistoryOrders> history) {
        try {
            if(history.size() > 0) repository.deleteAll(history);
            return "Sucesso ao apagar o histórico!";
        } catch (Exception e) {
            log.error("ERRO no delete do histórico " + e);
            throw new RuntimeException("Erro ao obter os dados do histórico, tente novamente.");
        }
    }
}
