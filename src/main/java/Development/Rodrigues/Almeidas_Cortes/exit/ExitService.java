package Development.Rodrigues.Almeidas_Cortes.exit;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.dto.CreateExitDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.dto.SearchDateDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.dto.UpdateExitDTO;
import Development.Rodrigues.Almeidas_Cortes.exit.entities.Exit;
import Development.Rodrigues.Almeidas_Cortes.exit.entities.SendExit;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.HistoryOrderService;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;

@Service
public class ExitService {
    @Autowired
    ExitRepository repository;

    private static final Logger log = LoggerFactory.getLogger(HistoryOrderService.class);

    public ResponseDTO getExistsDayService(SearchDateDTO dados) {
        try {
            LocalDate date = LocalDate.parse(dados.date());
            
            List<Exit> exist = repository.findByDataCompra(date);

            if(!exist.isEmpty()) {
                List<SendExit> newList = exist.stream()
                    .map(exit -> new SendExit(exit))
                    .collect(Collectors.toList());

                return new ResponseDTO(newList, "", "", "");
            }
            return new ResponseDTO("", "", "", "Nenhuma saída registrada para a data informada.");
        } catch (Exception e) {
            log.error("ERRO ao buscas os modelos " + e);
            throw new RuntimeException("Erro ao buscar as saídas, tente novamente.");
        }
    }

    public ResponseDTO getExitByIdService(Long id) {
        try {
            Optional<Exit> exitOp = repository.findById(id);

            if(exitOp.isPresent()) {
                SendExit exit = new SendExit(exitOp.get());
                return new ResponseDTO(exit, "", "", "");
            }

            return new ResponseDTO("", "Informações incorretas!", "", "");
        } catch (Exception e) {
            log.error("ERRO ao buscas o lançamento " + e);
            throw new RuntimeException("Erro ao buscar o lançamento, tente novamente.");
        }
    }

    public ResponseDTO createExitService(CreateExitDTO dados) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            Exit newExit = new Exit(dados, (User) authentication.getPrincipal());
            repository.save(newExit);

            return new ResponseDTO("", "", "Lançamento registrado com sucesso!", "");
        } catch (Exception e) {
            log.error("ERRO ao salvar o lançamento " + e);
            throw new RuntimeException("Erro ao salvar o lançamento, tente novamente.");
        }
    }

        public ResponseDTO updateExitService(UpdateExitDTO dados) {
        try {
            Optional<Exit> exitOp = repository.findById(dados.id());
            
            if (exitOp.isPresent()) {
                Exit exit = exitOp.get();
                exit.updateExit(dados);

                repository.save(exit);
                return new ResponseDTO("", "", "Lançamento atualizado com sucesso!", "");
            } else {
                return new ResponseDTO("", "Lançamento não encontrado!", "", "");
            }
        } catch (Exception e) {
            log.error("ERRO ao atualizar o lançamento " + e);
            throw new RuntimeException("Erro ao atualizar o lançamento, tente novamente.");
        }
    }

}
