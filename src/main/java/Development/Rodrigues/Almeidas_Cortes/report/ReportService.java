package Development.Rodrigues.Almeidas_Cortes.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.HistoryOrderService;
import Development.Rodrigues.Almeidas_Cortes.materials.MaterialRepository;
import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import Development.Rodrigues.Almeidas_Cortes.order.repositories.OrderRepository;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesReport;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesSituationReport;
import Development.Rodrigues.Almeidas_Cortes.report.dto.ParamsFiltersReports;
import Development.Rodrigues.Almeidas_Cortes.report.entities.OrderReport;

@Service
public class ReportService {

    @Autowired
    OrderRepository repository;

    @Autowired
    MaterialRepository materialRepository;

    @Autowired
    private TemplateEngine templateEngine;

    private static final Logger log = LoggerFactory.getLogger(HistoryOrderService.class);

    @Value("${frontEnd.api}")
    private String frontendApi; 

    public ResponseDTO generateReportService(ParamsFiltersReports dados) {
        try {
            
            LocalDateTime initialDate = dados.period().size() > 0 ? 
                dados.period().get(0).withHour(0).withMinute(0).withSecond(0).withNano(0) : null;
            LocalDateTime finalDate = dados.period().size() > 0 ? 
                dados.period().get(1).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999) : null;
    
            List<Order> list = new ArrayList<Order>(); 
        
            boolean paid = dados.situation() == TypesSituationReport.PAGOS;
    
            if(dados.idPedidos().size() > 0) {
                try {
                    List<Order> consult = repository.findByIdIn(dados.idPedidos());
    
                    if(consult.size() > 0) {
                        List<OrderReport> dadosReport = createList(consult, dados);
        
                        byte[] response = generateReportFile(dadosReport, dados, formatDate(initialDate, "dd/MM/yyyy"), formatDate(finalDate, "dd/MM/yyyy"));
                        return new ResponseDTO(Base64.getEncoder().encodeToString(response), "", "", "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else if(dados.report() == TypesReport.FICHA_DE_CORTE || dados.report() == TypesReport.CLIENTE) {
                Optional<Order> consult = repository.findById(dados.idPedido());
    
                if(consult.isPresent()) list.add(consult.get());
            } else {
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
                        if(dados.situation() == TypesSituationReport.TODOS) {
                            list = repository.findByDataPedidoBetweenOrderByIdDesc(initialDate, finalDate);
                        } else {
                            list = paid ? 
                                repository.findByDataPedidoBetweenAndDataPagamentoIsNotNull(initialDate, finalDate) :
                                repository.findByDataPedidoBetweenAndDataPagamentoIsNull(initialDate, finalDate);
                        }
                        break;
                    case CLIENTE_E_PERÍODO:
                            list = dados.situation() == TypesSituationReport.TODOS ? 
                            repository.findByDataPedidoBetweenAndClientId(initialDate, finalDate, dados.client()) :
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
                            list = repository.findByDataPedidoBetweenOrderByIdDesc(initialDate, finalDate);
                        } else {
                            list = paid ?
                            repository.findByDataPedidoBetweenAndDataPagamentoIsNotNull(initialDate, finalDate) :
                            repository.findByDataPedidoBetweenAndDataPagamentoIsNull(initialDate, finalDate);
                        }
                        break;
                }    
    
            }
    
            if(list.size() == 0) return new ResponseDTO("", "", "", "Não existem pedidos para o filtro selecionado");
                
            try {
                List<OrderReport> dadosReport = createList(list, dados);
    
                byte[] response = generateReportFile(dadosReport, dados, formatDate(initialDate, "dd/MM/yyyy"), formatDate(finalDate, "dd/MM/yyyy"));
                return new ResponseDTO(Base64.getEncoder().encodeToString(response), "", "", "");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            log.error("ERRO ao GERAR o RELATÓRIO " + e);
            throw new RuntimeException("Erro gerar o relatório, tente novamente.");
        }
    } 

    private List<OrderReport> createList(List<Order> list, ParamsFiltersReports dados) {
        List<Material> materiaisList = materialRepository.findAll();

        return list.stream()
            .map(order -> {
                List<Long> idsRecebidos = Arrays.stream(
                            Optional.ofNullable(order.getTipoRecebido()).orElse("")
                            .split(","))
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .collect(Collectors.toList());

                String nomesMateriais = materiaisList.stream()
                    .filter(m -> idsRecebidos.contains(m.getId()))
                    .map(Material::getNome)
                    .map(String::toUpperCase)
                    .collect(Collectors.joining(", "));

                String cores = String.join(", ", order.getCor()).toUpperCase();

                return new OrderReport(
                    order.getId(),
                    order.getModelo(),
                    cores,
                    order.getTotalPares(),
                    order.getClient(),
                    order.getTotalDinheiro(),
                    formatDate(order.getDataPedido(),"dd/MM/yyyy 'às' HH:mm"),
                    getDayOfWeek(order.getDataPedido()),
                    order.getGrade(),
                    order.getTotalPares() * order.getModelo().getQtdPecasPar(),
                    order.getQuemAssinou() == null || order.getQuemAssinou().isBlank() ? "Pedido não retirado" : order.getQuemAssinou(),
                    formatDate(order.getDataRetirada(), "dd/MM/yyyy"),
                    getHoraRetirada(order.getDataRetirada()),
                    order.getModelo().getPreco(),
                    formatDate(order.getDataPagamento(), "dd/MM/yyyy"),
                    order.getObs(),
                    dados.quantidadeVias(),
                    order.getModelo().getQtdFaca(),
                    nomesMateriais,
                    order.getModelo().getRendimento(),
                    order.getModelo().getObs(),
                    order.getModelo().getRefOrdem(),
                    order.getModelo().getUnidadeMedida()
                );
            })
            .collect(Collectors.toList());
    }

    public byte[] generateReportFile(
        List<OrderReport> dados, 
        ParamsFiltersReports dadosFront, 
        String dataInicial, 
        String dataFinal
    ) throws IOException {
        try {
            Context context = new Context();
            context.setVariable("dados", dados); 

            String htmlContent;

            if(dadosFront.report() == TypesReport.FECHAMENTO_CLIENTE) {
                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                double total = 0, totalPago = 0, totalDevido = 0;
                int totalPares = 0;

                for (OrderReport order : dados) {
                    totalPares += order.getTotalPares();
                    total += Double.parseDouble(order.getTotalDinheiro().replace(",", "."));

                    if(!order.getDataPagamento().equals("Pedido não retirado")) totalPago += Double.parseDouble(order.getTotalDinheiro().replace(",", "."));
                }
                
                totalDevido = total - totalPago;

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDateTime diaSemanaInicial = LocalDate.parse(dados.get(0).getDataPedido(), formatter).atStartOfDay();
                LocalDateTime diaSemanaFinal = LocalDate.parse(dados.get(dados.size() - 1).getDataPedido(), formatter).atStartOfDay();
                
                context.setVariable("totalDinheiro", currencyFormat.format(total));
                context.setVariable("totalPago", currencyFormat.format(totalPago));
                context.setVariable("totalPares", totalPares);
                context.setVariable("totalDevido", totalDevido);
                context.setVariable("totalDevidoFormatado", currencyFormat.format(totalDevido));
                context.setVariable(
                    "periodo", "De " + 
                    dados.get(0).getDataPedido() + " - " + getDayOfWeek(diaSemanaInicial) + " até " + dados.get(dados.size() - 1).getDataPedido() + " - " + getDayOfWeek(diaSemanaFinal)
                );

                htmlContent = templateEngine.process("relatorioCliente", context);
            } else if(dadosFront.report() == TypesReport.FICHAS_GERAIS) {
                htmlContent = templateEngine.process("relatorioFichasGerais", context);
            } else if(dadosFront.report() == TypesReport.FICHA_DE_CORTE) {
                htmlContent = templateEngine.process("relatorioFichaCorte", context);
            } else{
                htmlContent = templateEngine.process("relatorioFicha", context);
            }

            return htmlToPdf(htmlContent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] htmlToPdf(String html) throws IOException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(html);
        renderer.layout();

        // Criar o PDF em um ByteArrayOutputStream
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        renderer.createPDF(byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    private static String formatDate(LocalDateTime dataPedido, String type) {
        if (dataPedido == null) return "Pedido não retirado";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(type);
        return dataPedido.format(formatter);
    }

    private static String getDayOfWeek(LocalDateTime dataPedido) {
        if (dataPedido == null) return "Pedido não retirado";
        String diaSemana = dataPedido.getDayOfWeek().toString();
        switch (diaSemana) {
            case "MONDAY": return "Segunda-feira";
            case "TUESDAY": return "Terça-feira";
            case "WEDNESDAY": return "Quarta-feira";
            case "THURSDAY": return "Quinta-feira";
            case "FRIDAY": return "Sexta-feira";
            case "SATURDAY": return "Sábado";
            case "SUNDAY": return "Domingo";
            default: return diaSemana;
        }
    }
    
    private static String getHoraRetirada(LocalDateTime dataRetirada) {
        if (dataRetirada != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            return dataRetirada.toLocalTime().format(formatter);
        }
        return "Hora não disponível";
    }
}
