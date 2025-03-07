package Development.Rodrigues.Almeidas_Cortes.order.dto;

import java.time.LocalDate;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CreateOrderDTO(
    
    @NotNull
    @Schema(example = "{\n" +
    "  \"id\": 1,\n" +
    "  \"nome\": \"Flávio da Silva\",\n" +
    "  \"razaoSocial\": \"Fofi's calçados LTDA\",\n" +
    "  \"telefone\": \"(18) xxxxx-xxxx\",\n" +
    "  \"ativo\": true,\n" +
    "  \"obs\": \"Paga todo dia 05\"\n" +
    "}")
    Client client,

    @NotNull
    @Schema(example = "{\n" +
    "  \"id\": 1,\n" +
    "  \"idClient\": {\n" +
    "    \"id\": 1,\n" +
    "    \"nome\": \"Flávio da Silva\",\n" +
    "    \"razaoSocial\": \"Fofi's calçados LTDA\",\n" +
    "    \"telefone\": \"(18) xxxxx-xxxx\",\n" +
    "    \"ativo\": true,\n" +
    "    \"obs\": \"Paga todo dia 05\"\n" +
    "  },\n" +
    "  \"preco\": \"0.45\",\n" +
    "  \"qtdPecasPar\": \"10\",\n" +
    "  \"redOrdem\": \"1030\",\n" +
    "  \"fotos\": \"1, 2, 4\",\n" +
    "  \"qtdFaca\": \"3\",\n" +
    "  \"rendimento\": \"1, 2, 3\",\n" +
    "  \"cronometragem\": \"4, 5, 6\",\n" +
    "  \"obs\": \"Paga todo dia 05\"\n" +
    "}")
    Model modelo,
    
    @PastOrPresent
    @Schema(example = "2025/02/02")
    LocalDate dataPedido,

    @Schema(example = "132456")
    String relatorio,

    @NotNull
    @Schema(example = "10.00")
    Double totalDinheiro,

    @NotNull
    @Schema(example = "1000")
    Long totalPares,
    
    @NotNull
    @Schema(example = "10000")
    Long totalPecas,
    
    @NotBlank
    @Schema(example = "31: 10, 32/33: 12, 34/35: 15")
    String grade,
    
    @Schema(example = "Atenção no cabedal")
    String obs,
    
    @Schema(example = "2025/02/08")
    LocalDate dataPagamento,
    
    @Schema(example = "1.5, 1.8, 30")
    String metragemRecebida,
    
    @Schema(example = "15, 18, 30")
    String tipoRecebido,
    
    @Schema(example = "0.5, 8, 10")
    String metragemFinalizado,
    
    @Schema(example = "20, 10, 10")
    String rendimento,
    
    @Schema(example = "Cliclano da silva")
    String quemAssinou
) {

}
