package Development.Rodrigues.Almeidas_Cortes.clients.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "DadosBasicosClient")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DadosBasicosClient {

    @Id
    private Long id;

    private String nome;
    private String telefone;
}
