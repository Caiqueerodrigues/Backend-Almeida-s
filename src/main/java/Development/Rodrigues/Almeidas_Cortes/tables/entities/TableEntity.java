package Development.Rodrigues.Almeidas_Cortes.tables.entities;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.tables.dto.CreateTableDTO;
import Development.Rodrigues.Almeidas_Cortes.tables.dto.DinamicTableDTO;
import Development.Rodrigues.Almeidas_Cortes.tables.dto.UpdateTableDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Table(name = "tab_tables")
@Entity(name = "Table")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class TableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_Client", nullable = false)
    private Client client;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String tabelas;
    
    @Column(name = "created_At")
    private LocalDateTime createdAt;

    @Column(name = "updated_At")
    private LocalDateTime updatedAt;

    
    public TableEntity(CreateTableDTO dados) {
        this.client = dados.cliente();
        this.tabelas = toJson(dados.tables());
        this.createdAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
        this.updatedAt = null;
    }

    public void updateTableEntity(
        Long id,
        Client client,
        List<DinamicTableDTO> tables 
    ) {
        this.id = id;
        this.client = client;
        this.tabelas = toJson(tables);
        this.updatedAt = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
    }

    private String toJson(List<DinamicTableDTO> tabelas) {
        try {
            // Usando Jackson para converter a lista para JSON
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(tabelas);
        } catch (Exception e) {
            throw new RuntimeException("Error converting to JSON", e);
        }
    }

    // public List<DinamicTableDTO> getTabelas() {
    //     try {
    //         // Usando Jackson para converter o JSON de volta para a lista de objetos
    //         ObjectMapper objectMapper = new ObjectMapper();
    //         return objectMapper.readValue(tabelas, objectMapper.getTypeFactory().constructCollectionType(List.class, DinamicTableDTO.class));
    //     } catch (Exception e) {
    //         throw new RuntimeException("Error converting JSON to List", e);
    //     }
    // }
}
