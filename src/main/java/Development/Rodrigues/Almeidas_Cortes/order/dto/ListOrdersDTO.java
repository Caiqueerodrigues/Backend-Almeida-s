package Development.Rodrigues.Almeidas_Cortes.order.dto;

import java.time.LocalDate;
import java.util.List;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;

public record ListOrdersDTO(
    Long id,
    Client client,
    Model modelo,
    LocalDate dataPedido,
    String relatorioCliente,
    Double totalDinheiro,
    Long totalPares,
    Long totalPecas,
    String grade,
    String obs,
    LocalDate dataPagamento,
    List<String> metragemRecebido,
    List<Object> tipoRecebido,
    List<String> metragemFinalizado,
    List<String> rendimentoParesMetro
) {
}
