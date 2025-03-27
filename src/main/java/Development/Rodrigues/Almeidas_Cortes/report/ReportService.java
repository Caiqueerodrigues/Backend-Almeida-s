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

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;

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

    private byte[] generateReportFile(List<Order> dados, String fontendApi) throws IOException {
        try {
            PDDocument document = new PDDocument();

            // Cria uma página no documento
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            // Cria o fluxo de conteúdo para adicionar no PDF
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            URL url = new URL(frontendApi + "images/logo_escrito.png");
            InputStream inputStream = url.openStream();
            PDImageXObject image = PDImageXObject.createFromByteArray(document, inputStream.readAllBytes(), "logo");

            // Define o tamanho da imagem (opcional)
            float imageWidth = 100; // Largura da imagem
            float imageHeight = 50; // Altura da imagem

            // Posiciona a imagem no canto superior esquerdo
            contentStream.drawImage(image, 20, page.getMediaBox().getHeight() - imageHeight - 20, imageWidth, imageHeight);

            // Define a cor do texto como vermelho
            contentStream.setNonStrokingColor(255, 0, 0); // RGB para vermelho

            // Definir a fonte antes de mostrar o texto
            PDType1Font font = PDType1Font.HELVETICA_BOLD;
            contentStream.setFont(font, 18); // 18 é o tamanho da fonte

            // Adiciona o texto ao documento
            String text = "PDF GERADO";
        
            // Centraliza o texto
            float x = (page.getMediaBox().getWidth() - 100) / 2; // Centraliza horizontalmente
            float y = page.getMediaBox().getHeight() - 100; // Posição Y no topo da página

            contentStream.beginText();
            contentStream.newLineAtOffset(x, y); // Define a posição do texto
            contentStream.showText(text); // Adiciona o texto ao documento
            contentStream.endText();

            // Fecha o fluxo de conteúdo
            contentStream.close();

            // Cria um ByteArrayOutputStream para armazenar o PDF gerado em memória
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Salva o documento no ByteArrayOutputStream
            document.save(byteArrayOutputStream);

            // Fecha o documento
            document.close();

            // Retorna o PDF gerado como byte[]
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //backup lib mais fraca
    // private byte[] generateReportFile(List<Order> dados) {
    //     // Cria um ByteArrayOutputStream para armazenar o PDF gerado em memória
    //     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    //     // Cria o documento PDF
    //     Document document = new Document();

    //     // Cria o escritor de PDF, que escreverá o PDF no ByteArrayOutputStream
    //     PdfWriter.getInstance(document, byteArrayOutputStream);

    //     // Abre o documento para adicionar conteúdo
    //     document.open();

    //     Paragraph orderDetails = new Paragraph("PDF GERADO");
    //         orderDetails.setAlignment(Element.ALIGN_CENTER);

    //     document.add(orderDetails);

    //     document.close();

    //     // Retorna o PDF gerado como byte[]
    //     return byteArrayOutputStream.toByteArray();
    // }
}
