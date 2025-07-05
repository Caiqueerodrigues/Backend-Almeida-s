package Development.Rodrigues.Almeidas_Cortes.models.dto;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record CreateModelDTO(

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

    @NotBlank
    @Schema(example = "Tênis")
    String tipo,

    @NotNull
    @Schema(example = "0.45")
    Double preco,

    @NotNull
    @Schema(example = "4")
    Long qtdPecasPar,

    @Schema(example = "13245678")
    String refOrdem,
    
    @Schema(example = "1, 12, 15")
    String fotos,

    @NotNull
    @Schema(example = "4")
    Long qtdFaca,

    @Schema(example = "12")
    String rendimento,

    @Schema(example = "120")
    String cronometragem,

    @Schema(example = "12 , 1, 10")
    String obs,

    @Schema(example = "Metros")
    String unidadeMedida
) {

}
