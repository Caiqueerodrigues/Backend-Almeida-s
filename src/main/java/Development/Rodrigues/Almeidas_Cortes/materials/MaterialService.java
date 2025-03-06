package Development.Rodrigues.Almeidas_Cortes.materials;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.CreateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.dto.UpdateMaterialDTO;
import Development.Rodrigues.Almeidas_Cortes.materials.entities.Material;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialService {
    
    @Autowired
    MaterialRepository materialRepository;

    public ResponseDTO getMaterialsService () {
        List<Material> exists = materialRepository.findAll();

        if(exists.size() > 0) return new ResponseDTO(exists, "", "", "");
        else return new ResponseDTO("", "", "", "Não existem materials cadastrados!");
    }

    public ResponseDTO getMateriaIdService(Long id) {
        Optional<Material> material = materialRepository.findById(id);

        if(material.isPresent()) return new ResponseDTO(material, "", "", "");
        else return new ResponseDTO("", "", "", "Dados informados incorretos!");
    }

    public ResponseDTO getMaterialsActiveService () {
        List<Material> ativos = materialRepository.findByAtivo(true);

        if(ativos.size() > 0) return new ResponseDTO(ativos, "", "", "");
        else return new ResponseDTO("", "", "", "Não existem materials ativos!");
    }

    public ResponseDTO createMaterialService(CreateMaterialDTO dados) {
        if (materialRepository.findByNome(dados.nome()).isEmpty()) {
            materialRepository.save(new Material(dados));
            return new ResponseDTO("", "", "Material cadastrado com sucesso!", "");
        }
        return new ResponseDTO("", "", "", "MAterial já cadastrado anteriormente!");
    }

    public ResponseDTO putMaterialService(UpdateMaterialDTO dados) {
        Optional<Material> materialOptional = materialRepository.findById(dados.id());
    
        if (materialOptional.isPresent()) {
            Material material = materialOptional.get();
                material.updateMaterial(dados);
            materialRepository.save(material);
            return new ResponseDTO("", "", "Material alterado com sucesso!", "");
        }
        
        return new ResponseDTO("", "", "", "Material não encontrado!");
    }
}
