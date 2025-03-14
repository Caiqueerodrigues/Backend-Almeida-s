package Development.Rodrigues.Almeidas_Cortes.report;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Development.Rodrigues.Almeidas_Cortes.AlmeidasCortesApplication;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.order.OrderRepository;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesSituationReport;
import Development.Rodrigues.Almeidas_Cortes.report.dto.ParamsFiltersReports;

@Service
public class ReportService {

    @Autowired
    OrderRepository repository;

    public ResponseDTO generateReportService(ParamsFiltersReports dados) {
        LocalDateTime initialDate = dados.period().size() > 0 ? 
            dados.period().get(0).withHour(0).withMinute(0).withSecond(0).withNano(0) : null;
        LocalDateTime finalDate = dados.period().size() > 0 ? 
            dados.period().get(1).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999) : null;

            List<Order> list = new ArrayList<Order>(); 
        
            boolean paid = dados.situation() == TypesSituationReport.PAGOS;

            switch (dados.firstFilter()) {
                case CLIENTE:
                    if(dados.situation() == TypesSituationReport.TODOS) {
                        list = repository.findByClientId(dados.client());
                    } else {
                        list = paid ?
                        repository.findByClientIdAndDataPagamentoIsNotNull(dados.client()) :
                        repository.findByClientIdAndDataPagamentoIsNull(dados.client());
                    }
                    break;
                case PERÍODO:
                    list = paid ? 
                        repository.findByDataPedidoBetweenAndDataPagamentoIsNotNull(initialDate, finalDate) :
                        repository.findByDataPedidoBetweenAndDataPagamentoIsNull(initialDate, finalDate);
                    break;
                case CLIENTE_E_PERÍODO:
                        list = dados.situation() == TypesSituationReport.TODOS ? 
                        repository.findByClientId(dados.client()) :
                        repository.findOrdersByRangeByClientPaidOrNot(initialDate, finalDate, dados.client(), paid);
                    break;
                case SITUAÇÃO:
                    if(dados.situation() == TypesSituationReport.TODOS) {
                        list = repository.findAll();
                    } else {
                        list = paid ?
                        repository.findByDataPagamentoIsNotNull() :
                        repository.findByDataPagamentoIsNull();
                    }
                    break;
                default:
                    if(dados.situation() == TypesSituationReport.TODOS) {
                        list = repository.findByDataPedidoBetween(initialDate, finalDate);
                    } else {
                        list = paid ?
                        repository.findByDataPedidoBetweenAndDataPagamentoIsNotNull(initialDate, finalDate) :
                        repository.findByDataPedidoBetweenAndDataPagamentoIsNull(initialDate, finalDate);
                    }
                    break;
            }
            
        return list.size() > 0 ? 
            new ResponseDTO(list, "", "Sucesso na requisição", "") :
            new ResponseDTO("", "", "", "Não existem pedidos para o filtro selecionado");
    } 
}
