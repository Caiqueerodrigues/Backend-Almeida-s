package Development.Rodrigues.Almeidas_Cortes.clients;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.clients.dto.CreateClientDTO;
import Development.Rodrigues.Almeidas_Cortes.clients.dto.DadosBasicosClientDTO;
import Development.Rodrigues.Almeidas_Cortes.clients.dto.UpdateClientDTO;
import Development.Rodrigues.Almeidas_Cortes.clients.entities.Client;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;

@Service
public class ClientService {

    @Autowired
    ClientRepository repository;

    public ResponseDTO getAllClientsService() {
        List<Client> clients = repository.findAll();

        if(clients.size() > 0) {
            List<DadosBasicosClientDTO> responseList = clients.stream()
                    .map(client -> new DadosBasicosClientDTO(client.getId(), client.getNome(), client.getTelefone()))
                    .collect(Collectors.toList());
            return new ResponseDTO(responseList, "", "", "");
        }

        return new ResponseDTO("", "", "", "Não existem clientes cadastrados!");
    }

    public ResponseDTO getAllClientsActiveService() {
        List<Client> clients = repository.findByAtivo(true);

        if(clients.size() > 0) {
        //     List<DadosBasicosClientDTO> responseList = clients.stream()
        //             .map(client -> new DadosBasicosClientDTO(client.getId(), client.getNome(), client.getTelefone()))
        //             .collect(Collectors.toList());
        //     return new ResponseDTO(responseList, "", "", "");
            return new ResponseDTO(clients, "", "", "");
        }

        return new ResponseDTO("", "", "", "Não existem clientes ativos cadastrados!");
    }

    public ResponseDTO getClientIdService(Long id) {
        Optional<Client> exist = repository.findById(id);

        if(exist.isPresent()) return new ResponseDTO(exist, "", "", "");

        return new ResponseDTO("", "Dados informados incorretos!", "", "");
    }

    public ResponseDTO createClientService(CreateClientDTO dados) {
        Optional<Client> exist = repository.findByNome(dados.nome());

        if(exist.isPresent()) return new ResponseDTO("", "Cliente já cadastrado anteriormente!", "", "");

        Client newClient = new Client(dados);
        repository.save(newClient);

        return new ResponseDTO("", "", "Cliente cadastrado com sucesso!", "");
    }

    public ResponseDTO updateClientService(UpdateClientDTO dados) {
        Optional<Client> exist = repository.findById(dados.id());

        if(exist.isPresent()) {
            Client client = exist.get();
            client.updateClient(dados);

            repository.save(client);

            return new ResponseDTO("", "", "Cliente atualizado com sucesso!", "");
        }

        return new ResponseDTO("", "Dados informados incorretos!", "", "");
    }
}
