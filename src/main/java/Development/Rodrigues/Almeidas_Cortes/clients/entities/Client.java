package Development.Rodrigues.Almeidas_Cortes.clients.entities;

import Development.Rodrigues.Almeidas_Cortes.clients.dto.CreateClientDTO;
import Development.Rodrigues.Almeidas_Cortes.clients.dto.UpdateClientDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.CreateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.UpdateMaterialDTO;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tab_clients")
@Entity(name = "Clients")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, length = 250)
    private String nome;

    @Column(length = 100)
    private String razaoSocial;

    @Column(nullable = false, length = 25)
    private String telefone;

    @Column(nullable = false)
    private boolean ativo;

    @Column(length = 255)
    private String obs;

    public Client(CreateClientDTO dados) {
        this.nome = dados.nome();
        this.razaoSocial = dados.razaoSocial();
        this.telefone = dados.telefone();
        this.ativo = dados.ativo();
        this.obs = dados.obs();
    }

    public void updateClient(UpdateClientDTO dados) {
        this.nome = dados.nome();
        this.telefone = dados.telefone();
        this.ativo = dados.ativo();
        if(!dados.razaoSocial().isBlank()) this.razaoSocial = dados.razaoSocial();
        if(!dados.obs().isBlank()) this.obs = dados.obs();
    }
}
