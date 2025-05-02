package Development.Rodrigues.Almeidas_Cortes.tables;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.tables.entities.TableEntity;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TablesService {
    @Autowired
    private TablesRepository tablesRepository;

    public ResponseDTO getTablesClient(Long idClient) {
        Optional<TableEntity> tables = tablesRepository.findByClientId(idClient);
        if (tables.isEmpty()) {
            return new ResponseDTO("", "Nenhuma tabela encontrada!", "", "");
        } else {
            return new ResponseDTO(tables, "", "", "");
        }
    }

}
