package Development.Rodrigues.Almeidas_Cortes.models.entities;

import java.util.List;

import Development.Rodrigues.Almeidas_Cortes.anexos.entities.Anexo;
import Development.Rodrigues.Almeidas_Cortes.anexos.entities.SendAnexo;
import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SendModel {

    @Id
    private Long id;

    private Client client;

    private String tipo;
    
    private Double preco;

    private Long qtdPecasPar;

    private String refOrdem;

    private List<SendAnexo> fotos;

    private Long qtdFaca;

    private String rendimento;

    private String cronometragem;

    private String obs;

    public SendModel(
        Long id,
        Client client,
        String tipo,
        double preco,
        Long qtdPecasPar,
        String ordem,
        List<SendAnexo> fotos,
        Long qtdFaca,
        String rendimento,
        String cronometragem,
        String obs
    ) {
        this.id = id;
        this.client = client;
        this.tipo = tipo;
        this.preco = preco;
        this.qtdPecasPar = qtdPecasPar;
        this.refOrdem = ordem;
        this.fotos = fotos;
        this.qtdFaca = qtdFaca;
        this.rendimento = rendimento;
        this.cronometragem = cronometragem;
        this.obs = obs;
    }
}
