package Development.Rodrigues.Almeidas_Cortes.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.anexos.AnexosService;
import Development.Rodrigues.Almeidas_Cortes.anexos.entities.Anexo;
import Development.Rodrigues.Almeidas_Cortes.anexos.entities.SendAnexo;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.HistoryOrderService;
import Development.Rodrigues.Almeidas_Cortes.models.dto.CreateModelDTO;
import Development.Rodrigues.Almeidas_Cortes.models.dto.UpdateModelDTO;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import Development.Rodrigues.Almeidas_Cortes.models.entities.SendModel;

@Service
public class ModelService {

    @Autowired
    ModelRepository repository;

    @Autowired
    AnexosService anexosService;

    private static final Logger log = LoggerFactory.getLogger(HistoryOrderService.class);


    public ResponseDTO getAllModelsClientService(Long id) {
        try {
            List<Model> list = repository.findByClient_IdOrderByTipoAsc(id);
            
            if(list.size() > 0) {
                return new ResponseDTO(list, "", "", "");
            } 
    
            return new ResponseDTO("", "", "", "Não existem modelos cadastrados para este cliente!");
        } catch (Exception e) {
            log.error("ERRO ao buscas os modelos " + e);
            throw new RuntimeException("Erro ao buscar os modelos do cliente, tente novamente.");
        }
    }

    public ResponseDTO createModelClient(CreateModelDTO dados) {
        try {
            Model newModel = new Model(dados);
            repository.save(newModel);
    
            return new ResponseDTO("", "", "Modelo cadastrado com sucesso!", "");
        } catch (Exception e) {
            log.error("ERRO ao cadastrar o modelo " + e);
            throw new RuntimeException("Erro ao cadastrar o modelo, tente novamente.");
        }
    }

    public ResponseDTO getModelIdService(Long id) {
        try {
            
            Optional<Model> list = repository.findById(id);
            
            if(list.isPresent()) {
                Model model = list.get();
                List<SendAnexo> listAnexo = new ArrayList<SendAnexo>();
    
                if(model.getFotos() != null && !model.getFotos().isEmpty()) {
                    listAnexo = anexosService.getPhotosService(model.getFotos(), model.getId(), model.getClient().getId());
                }
                
                SendModel response = new SendModel(
                    model.getId(),
                    model.getClient(),
                    model.getTipo(),
                    model.getPreco(),
                    model.getQtdPecasPar(),
                    model.getRefOrdem(),
                    listAnexo,
                    model.getQtdFaca(),
                    model.getRendimento(),
                    model.getCronometragem(),
                    model.getObs()
                );
    
                return new ResponseDTO(response, "", "", "");
            }
    
            return new ResponseDTO("", "", "", "Não existem modelos cadastrados para este cliente!");
        } catch (Exception e) {
            log.error("ERRO ao buscar o modelo pelo cliente " + e);
            throw new RuntimeException("Erro ao buscar o modelo, tente novamente.");
        }
    }

    public ResponseDTO updateModelService(UpdateModelDTO dados) {
        try {
            Optional<Model> exists = repository.findById(dados.id());
    
            if(exists.isPresent()) {
                Model model = exists.get();
                model.updateModel(dados);
    
                repository.save(model);
    
                return new ResponseDTO("", "", "Modelo atualizado com sucesso!", "");
            }
    
            return new ResponseDTO("", "Dados informados incorretos!", "", "");
        } catch (Exception e) {
            log.error("ERRO ao alterar o modelo " + e);
            throw new RuntimeException("Erro ao alterar o modelo, tente novamente.");
        }
    }

}
