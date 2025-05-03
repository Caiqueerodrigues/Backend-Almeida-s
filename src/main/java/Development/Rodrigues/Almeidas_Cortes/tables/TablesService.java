package Development.Rodrigues.Almeidas_Cortes.tables;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.tables.dto.CreateTableDTO;
import Development.Rodrigues.Almeidas_Cortes.tables.dto.DinamicTableDTO;
import Development.Rodrigues.Almeidas_Cortes.tables.entities.TableEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TablesService {
    @Autowired
    private TablesRepository tablesRepository;

    public ResponseDTO getTablesClientService(Long idClient) {
        List<TableEntity>  tables = tablesRepository.findByClientId(idClient);
        if (tables.size() == 0) {
            return new ResponseDTO("", "Nenhuma tabela encontrada!", "", "");
        } else {
            return new ResponseDTO(tables, "", "", "");
        }
    }

    public ResponseDTO createUpdateTableService(CreateTableDTO dados) {
        for (DinamicTableDTO table : dados.tables()) {
            if(table.isNew()) {
                TableEntity newTable = new TableEntity(dados);
                tablesRepository.save(newTable);
            } else {
                updateTable(table, dados);
            }
        }

        if(!dados.idsDeletados().isEmpty()) {
            deleteTable(dados.idsDeletados());
        }
        return new ResponseDTO("", "", "Tabela(s) salva(s) com sucesso!", "");
    }

    private void updateTable(DinamicTableDTO table, CreateTableDTO dados) {
        Optional<TableEntity> tableEntity = tablesRepository.findById(Long.parseLong(table.id()));
        if (tableEntity.isPresent()) {
            TableEntity existingTable = tableEntity.get();
            existingTable.updateTableEntity(existingTable.getId(), dados.cliente(), dados.tables());
            tablesRepository.save(existingTable);
        }
    }

    private void deleteTable(List<Long> ids) {
        List<TableEntity> tablesToDelete = tablesRepository.findAllById(ids);
        if (!tablesToDelete.isEmpty()) {
            tablesRepository.deleteAll(tablesToDelete);
        }
    }
}
