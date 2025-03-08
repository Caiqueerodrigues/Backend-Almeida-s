package Development.Rodrigues.Almeidas_Cortes.anexos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Development.Rodrigues.Almeidas_Cortes.anexos.dto.AnexoDTO;
import Development.Rodrigues.Almeidas_Cortes.anexos.dto.ListAnexosDTO;
import Development.Rodrigues.Almeidas_Cortes.anexos.dto.VariosAnexosDTO;
import Development.Rodrigues.Almeidas_Cortes.anexos.entities.Anexo;
import Development.Rodrigues.Almeidas_Cortes.anexos.entities.SendAnexo;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.models.ModelRepository;
import Development.Rodrigues.Almeidas_Cortes.models.dto.UpdateModelDTO;
import Development.Rodrigues.Almeidas_Cortes.models.entities.Model;
import jakarta.validation.Valid;

@Service
public class AnexosService {
    
    @Autowired
    AnexoRepository repository;

    @Autowired
    ModelRepository modelRepository;

    @Value("${upload.dir.photos}") 
    private String uploadDir;
    
    @Value("${backend.api}")
    private String backendApi; 

    public ResponseDTO insertImageModelService(@Valid VariosAnexosDTO dados) {
        try {
            String targetDir = uploadDir + dados.idClient() + "/" + dados.idModelo();

            String listId = saveFileToDisk(dados, targetDir);

            return new ResponseDTO(listId, "", "Foto(s) anexada(s) com sucecsso!", "");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo " + e);
        }
    }

    public Resource getPhotoService(String model, String file, String idCliente) {
        try {
            String targetDir = uploadDir + File.separator + idCliente + File.separator + model + File.separator + file;
            Path path = Paths.get(targetDir);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Não foi possível ler o arquivo: " + model + "/" + file);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private String saveFileToDisk(VariosAnexosDTO dados, String targetDir) throws IOException {
        try {
            File directory = new File(targetDir);
            String idsAnexos = "";
            for(int i = 0; i < dados.files().size(); i ++) {
                MultipartFile file = dados.files().get(i).file();

                boolean newImage = true;
                String fileName = generateUniqueFileName(dados.files().get(i).originalName(), dados.nomePeca().get(i));
                
                if (!directory.exists()) {
                    directory.mkdirs();
                } else {
                    String baseName = fileName.substring(0, fileName.indexOf('_'));
                    File[] files = directory.listFiles();
                    
                    for (File existingFile : files) {
                        if (existingFile.getName().split("_")[0].equalsIgnoreCase(baseName)) {
                            fileName = existingFile.getName();
                            existingFile.delete();
                            newImage = false;
                            break;
                        }
                    }
                }
                System.out.println("BYTES !#" + file.getBytes());

                Path targetPath = Paths.get(targetDir, fileName);
                Files.write(targetPath, file.getBytes());
                if(newImage) {
                    idsAnexos = saveAnexoDataBase(
                        dados, 
                        fileName, 
                        dados.nomePeca().get(i), 
                        dados.propriedadeFaca().get(i),
                        dados.qtdPar().get(i),
                        dados.precoFaca().get(i),
                        dados.obs().get(i)
                    );
                } 
            }
            return idsAnexos;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo " + e);
        }
    }

    private String saveAnexoDataBase(
            VariosAnexosDTO dados, 
            String fileName, 
            String nomePecaString, 
            String propriedadeFaca, 
            Long qtdPar,
            Double precoFaca,
            String obs
        ) {
        try {
            Optional<Model> model = modelRepository.findById(dados.idModelo());

            Anexo newAnexo = new Anexo(
                fileName,
                model.get(),
                nomePecaString,
                qtdPar,
                propriedadeFaca,
                precoFaca,
                obs
            );

            repository.save(newAnexo);

            Model modelUpdate = model.get();
            String anexoId = modelUpdate.getFotos() != null && !modelUpdate.getFotos().isEmpty() ?
                modelUpdate.getFotos() + "," + newAnexo.getId() :
                Long.toString(newAnexo.getId());

            UpdateModelDTO newDados = new UpdateModelDTO(
                modelUpdate.getId(), 
                modelUpdate.getClient(), 
                modelUpdate.getTipo(), 
                modelUpdate.getPreco(),
                modelUpdate.getQtdPecasPar(), 
                modelUpdate.getRefOrdem(), 
                anexoId, 
                modelUpdate.getQtdFaca(), 
                modelUpdate.getRendimento(), 
                modelUpdate.getCronometragem(), 
                modelUpdate.getObs()
            );

            modelUpdate.updateModel(newDados);
            modelRepository.save(modelUpdate);

            return anexoId;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o arquivo", e);
        }
    }

    public List<SendAnexo> getPhotosService(String ids, Long idModelo, Long idCliente ) {
        List<Long> anexoIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        
        List<Anexo> anexos = repository.findByIdIn(anexoIds);
    
        List<SendAnexo> sendAnexos = anexos.stream()
            .map(anexo -> {
                String fileName = anexo.getNomeFile();
                String photoUrl = backendApi + "anexo/" + idCliente + "/" + idModelo + "/" + fileName;

                SendAnexo sendAnexo = new SendAnexo(
                    anexo.getId(),
                    anexo.getNomeFile(),
                    anexo.getIdModelo(),
                    anexo.getNomePeca(),
                    anexo.getQtdPar(),
                    anexo.getPropriedadeFaca(),
                    anexo.getPrecoFaca(),
                    anexo.getObs(),
                    photoUrl 
                );
                return sendAnexo;
            })
            .collect(Collectors.toList());
    
        return sendAnexos;
    }

    private String generateUniqueFileName(String originalFileName, String nomePeca) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String uniqueFileName = nomePeca + "_" + UUID.randomUUID().toString().replace("-", "") + extension;
        return uniqueFileName;
    }
}
