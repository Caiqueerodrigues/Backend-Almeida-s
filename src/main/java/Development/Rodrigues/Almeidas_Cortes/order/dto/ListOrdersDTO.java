package Development.Rodrigues.Almeidas_Cortes.order.dto;

import java.time.LocalDateTime;
import java.util.List;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;

public record ListOrdersDTO(
    Long id,
    Client client,
    Model modelo,
    LocalDateTime dataPedido,
    LocalDateTime dataFinalizado,
    String relatorioCliente,
    Double totalDinheiro,
    Double totalPares,
    String grade,
    List<String> cor,
    String obs,
    LocalDateTime dataPagamento,
    List<Object> tipoRecebido,
    List<String> rendimentoParesMetro,
    String quemAssinou,
    String quemCortou,
    String jaFoiPago,
    String categoria
) {
}
