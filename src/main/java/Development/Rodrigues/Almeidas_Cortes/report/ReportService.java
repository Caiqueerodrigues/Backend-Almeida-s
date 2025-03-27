package Development.Rodrigues.Almeidas_Cortes.report;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.order.OrderRepository;
import Development.Rodrigues.Almeidas_Cortes.order.entities.Order;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesReport;
import Development.Rodrigues.Almeidas_Cortes.report.Enums.TypesSituationReport;
import Development.Rodrigues.Almeidas_Cortes.report.dto.ParamsFiltersReports;

@Service
public class ReportService {

    @Autowired
    OrderRepository repository;

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${frontEnd.api}")
    private String frontendApi; 

    public ResponseDTO generateReportService(ParamsFiltersReports dados) {
        LocalDateTime initialDate = dados.period().size() > 0 ? 
            dados.period().get(0).withHour(0).withMinute(0).withSecond(0).withNano(0) : null;
        LocalDateTime finalDate = dados.period().size() > 0 ? 
            dados.period().get(1).withHour(23).withMinute(59).withSecond(59).withNano(999_999_999) : null;

        List<Order> list = new ArrayList<Order>(); 
    
        boolean paid = dados.situation() == TypesSituationReport.PAGOS;

        if(dados.report() == TypesReport.FICHA_DE_CORTE || dados.report() == TypesReport.CLIENTE) {
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

        }

        if(list.size() == 0) return new ResponseDTO("", "", "", "Não existem pedidos para o filtro selecionado");
            
        try {
            byte[] response = generateReportFile(list, frontendApi);
            return new ResponseDTO(Base64.getEncoder().encodeToString(response), "", "", "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    } 

    public byte[] generateReportFile(List<Order> dados, String frontendApi) throws IOException {
        try {
            Context context = new Context();
            context.setVariable("dados", dados); 
            context.setVariable("frontendApi", frontendApi);

            String htmlContent = templateEngine.process("fichaCorte", context);  // O nome correto do seu template

            // HTML gerado, vamos gerar o PDF com Flying Saucer
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
}
