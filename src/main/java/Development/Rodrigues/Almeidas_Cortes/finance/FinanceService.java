package Development.Rodrigues.Almeidas_Cortes.finance;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.ExitRepository;
import Development.Rodrigues.Almeidas_Cortes.exit.entities.Exit;
import Development.Rodrigues.Almeidas_Cortes.exit.enums.TipoServico;
import Development.Rodrigues.Almeidas_Cortes.finance.entities.FinanceGraph;
import Development.Rodrigues.Almeidas_Cortes.finance.entities.FinanceGraph.GraphData;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.HistoryOrderService;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import Development.Rodrigues.Almeidas_Cortes.order.repositories.OrderRepository;

@Service
public class FinanceService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ExitRepository exitRepository;

    private static final Logger log = LoggerFactory.getLogger(HistoryOrderService.class);

    public ResponseDTO getFinanceDataService(LocalDate initialDate, LocalDate finalDate) {
        try {
            List<Exit> exits = exitRepository.findByDataCompraBetweenAndDeletedIsFalseOrderByDataCompra(initialDate , finalDate);

            LocalDateTime startDateTime = initialDate.atStartOfDay();
            LocalDateTime endDateTime = finalDate.atTime(23, 59, 59, 999_999_999);
            List<Order> orders = orderRepository.findByDataPedidoBetweenOrderByIdDesc(startDateTime , endDateTime);
            List<Order> ordersPaid = orderRepository.findByDataPedidoBetweenAndDataPagamentoIsNotNullOrderByIdDesc(startDateTime , endDateTime);

            if(!exits.isEmpty() || !orders.isEmpty()) {
                List<String> labels = initialDate.datesUntil(finalDate.plusDays(1))
                    .map(date -> String.format("%02d/%02d", date.getDayOfMonth(), date.getMonthValue()))
                    .toList();

                FinanceGraph bar = exitTreatment(exits, orders, ordersPaid, labels, initialDate);
                return new ResponseDTO(bar, "", "", "");
            }

            return new ResponseDTO("", "", "", "Nenhuma informação encontrada para este período.");
        } catch (Exception e) {
            log.error("ERRO no get das Finanças " + e);
            throw new RuntimeException("Erro ao obter os dados das finanças, tente novamente." + e);
        }
    }

    private FinanceGraph exitTreatment(List<Exit> dados, List<Order> orders, List<Order> ordersPaid, List<String> labels, LocalDate initialDate) {
        List<LocalDate> dias = initialDate.datesUntil(initialDate.plusDays(labels.size()))
            .toList();

        // LINE
        List<Double> exits = dias.stream()
            .map(date -> {
                double sum = dados.stream()
                    .filter(exit -> exit.getDataCompra().isEqual(date))
                    .map(Exit::getValorCompra)
                    .reduce(0.0, Double::sum);
                return round2(sum);
            })
            .toList();
        List<Double> ordersValues = dias.stream()
            .map(date -> {
                double sum = orders.stream()
                    .filter(order -> order.getDataPedido().toLocalDate().isEqual(date))
                    .map(Order::getTotalDinheiro)
                    .reduce(0.0, Double::sum);
                return round2(sum);
            })
            .toList();
        Double totalExits = round2(exits.stream().reduce(0.0, Double::sum));
        Double totalOrders = round2(ordersValues.stream().reduce(0.0, Double::sum));
        List<FinanceGraph.GraphData> dataLine = List.of(
            new FinanceGraph.GraphData("Receitas R$ " + String.format("%.2f", totalOrders), ordersValues),
            new FinanceGraph.GraphData("Despesas R$ " + String.format("%.2f", totalExits), exits)
        );

        // BAR
        List<Double> ordersCortes = dias.stream()
            .map(date -> {
                double sum = orders.stream()
                    .filter(order -> order.getCategoria().equals(String.valueOf(TipoServico.Corte)) &&
                                    order.getDataPedido().toLocalDate().isEqual(date))
                    .map(Order::getTotalDinheiro)
                    .reduce(0.0, Double::sum);
                return round2(sum);
            })
            .toList();
        Double totalCortesBar = round2(ordersCortes.stream().reduce(0.0, Double::sum));
        List<Double> ordersDebruagem = dias.stream()
            .map(date -> {
                double sum = orders.stream()
                    .filter(order -> order.getCategoria().equals(String.valueOf(TipoServico.Debruagem)) &&
                                    order.getDataPedido().toLocalDate().isEqual(date))
                    .map(Order::getTotalDinheiro)
                    .reduce(0.0, Double::sum);
                return round2(sum);
            })
            .toList();
        Double totalDebruagemBar = round2(ordersDebruagem.stream().reduce(0.0, Double::sum));
        List<Double> ordersDublagem = dias.stream()
            .map(date -> {
                double sum = orders.stream()
                    .filter(order -> order.getCategoria().equals(String.valueOf(TipoServico.Dublagem)) &&
                                    order.getDataPedido().toLocalDate().isEqual(date))
                    .map(Order::getTotalDinheiro)
                    .reduce(0.0, Double::sum);
                return round2(sum);
            })
            .toList();
        Double totalDublagemBar = round2(ordersDublagem.stream().reduce(0.0, Double::sum));

        List<FinanceGraph.GraphData> dataBar = List.of(
            new FinanceGraph.GraphData("Corte R$ " + String.format("%.2f", totalCortesBar), ordersCortes),
            new FinanceGraph.GraphData("Debruagem R$ " + String.format("%.2f", totalDebruagemBar), ordersDebruagem),
            new FinanceGraph.GraphData("Dublagem R$ " + String.format("%.2f", totalDublagemBar), ordersDublagem)
        );

        // BAR PAID
        List<Double> ordersCortesPaid = dias.stream()
            .map(date -> {
                double sum = ordersPaid.stream()
                    .filter(order -> order.getCategoria().equals(String.valueOf(TipoServico.Corte)) &&
                                    order.getDataPedido().toLocalDate().isEqual(date))
                    .map(Order::getTotalDinheiro)
                    .reduce(0.0, Double::sum);
                return round2(sum);
            })
            .toList();
        Double totalCortesPaidBar = round2(ordersCortesPaid.stream().reduce(0.0, Double::sum));
        List<Double> ordersDebruagemPaid = dias.stream()
            .map(date -> {
                double sum = ordersPaid.stream()
                    .filter(order -> order.getCategoria().equals(String.valueOf(TipoServico.Debruagem)) &&
                                    order.getDataPedido().toLocalDate().isEqual(date))
                    .map(Order::getTotalDinheiro)
                    .reduce(0.0, Double::sum);
                return round2(sum);
            })
            .toList();
        Double totalDebruagemPaidBar = round2(ordersDebruagemPaid.stream().reduce(0.0, Double::sum));
        List<Double> ordersDublagemPaid = dias.stream()
            .map(date -> {
                double sum = ordersPaid.stream()
                    .filter(order -> order.getCategoria().equals(String.valueOf(TipoServico.Dublagem)) &&
                                    order.getDataPedido().toLocalDate().isEqual(date))
                    .map(Order::getTotalDinheiro)
                    .reduce(0.0, Double::sum);
                return round2(sum);
            })
            .toList();
        Double totalDublagemPaidBar = round2(ordersDublagemPaid.stream().reduce(0.0, Double::sum));

        List<FinanceGraph.GraphData> dataPaidBar = List.of(
            new FinanceGraph.GraphData("Corte R$ " + String.format("%.2f", totalCortesPaidBar), ordersCortesPaid),
            new FinanceGraph.GraphData("Debruagem R$ " + String.format("%.2f", totalDebruagemPaidBar), ordersDebruagemPaid),
            new FinanceGraph.GraphData("Dublagem R$ " + String.format("%.2f", totalDublagemPaidBar), ordersDublagemPaid)
        );

        // PIE
        double totalDublagem = round2(
            dados.stream()
                .filter(exit -> TipoServico.Dublagem.equals(exit.getTipoServico()))
                .map(Exit::getValorCompra)
                .reduce(0.0, Double::sum)
        );
        double totalDebruagem = round2(
            dados.stream()
                .filter(exit -> TipoServico.Debruagem.equals(exit.getTipoServico()))
                .map(Exit::getValorCompra)
                .reduce(0.0, Double::sum)
        );
        double totalCorte = round2(
            dados.stream()
                .filter(exit -> TipoServico.Corte.equals(exit.getTipoServico()))
                .map(Exit::getValorCompra)
                .reduce(0.0, Double::sum)
        );
        List<Double> dataPie = List.of(totalCorte, totalDebruagem, totalDublagem);

        return new FinanceGraph(labels, dataLine, dataBar, dataPie, dataPaidBar);
    }

    private Double round2(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
