package Development.Rodrigues.Almeidas_Cortes.materials;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.HistoryOrderService;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.CreateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.UpdateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialService {
    
    @Autowired
    MaterialRepository materialRepository;

    private static final Logger log = LoggerFactory.getLogger(HistoryOrderService.class);

    public ResponseDTO getMaterialsService () {
        try {
            List<Material> exists = materialRepository.findAllByOrderByNomeAsc();
    
            if(exists.size() > 0) return new ResponseDTO(exists, "", "", "");
            else return new ResponseDTO("", "", "", "Não existem materials cadastrados!");
        } catch (Exception e) {
            log.error("ERRO ao pegar os materiais " + e);
            throw new RuntimeException("Erro ao obter os materiais, tente novamente.");
        }
    }

    public ResponseDTO getMateriaIdService(Long id) {
        try {
            Optional<Material> material = materialRepository.findById(id);
    
            if(material.isPresent()) return new ResponseDTO(material, "", "", "");
            else return new ResponseDTO("", "", "", "Dados informados incorretos!");
        } catch (Exception e) {
            log.error("ERRO ao obter o material " + e);
            throw new RuntimeException("Erro ao obter o material, tente novamente.");
        }
    }

    public ResponseDTO createMaterialService(CreateMaterialDTO dados) {
        try {
            if (materialRepository.findByNome(dados.nome()).isEmpty()) {
                materialRepository.save(new Material(dados));
                return new ResponseDTO("", "", "Material cadastrado com sucesso!", "");
            }
            return new ResponseDTO("", "", "", "Material já cadastrado anteriormente!");
        } catch (Exception e) {
            log.error("ERRO ao cadastrar o material " + e);
            throw new RuntimeException("Erro ao cadastrar o material, tente novamente.");
        }
    }

    public ResponseDTO putMaterialService(UpdateMaterialDTO dados) {
        try {
            Optional<Material> materialOptional = materialRepository.findById(dados.id());
        
            if (materialOptional.isPresent()) {
                Material material = materialOptional.get();
                    material.updateMaterial(dados);
                materialRepository.save(material);
                return new ResponseDTO("", "", "Material alterado com sucesso!", "");
            }
            
            return new ResponseDTO("", "", "", "Material não encontrado!");
        } catch (Exception e) {
            log.error("ERRO ao alterar o material " + e);
            throw new RuntimeException("Erro ao alterar o material, tente novamente.");
        }
    }
}
