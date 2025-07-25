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

    @Value("${upload.dir.photosUsers}") 
    private String photosUserDir;
    
    @Value("${backend.api}")
    private String backendApi; 

    
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
    
    public Resource getPhotoPerfilService(String fileName) {
        try {
            String targetDir = photosUserDir + File.separator + File.separator + fileName;
            Path path = Paths.get(targetDir);
            Resource resource = new UrlResource(path.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Não foi possível ler o arquivo: " + fileName);
            }
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public ResponseDTO insertImageModelService(@Valid VariosAnexosDTO dados) {
        try {
            String targetDir = uploadDir + dados.idClient() + "/" + dados.idModelo();
            List<Long> listId = saveFileToDisk(dados, targetDir);

            return new ResponseDTO(listId, "", "Foto(s) anexada(s) com sucecsso!", "");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo " + e);
        }
    }

    private List<Long> saveFileToDisk(VariosAnexosDTO dados, String targetDir) throws IOException {
        try {
            File directory = new File(targetDir);
            List<Long> idsAnexos = new ArrayList<Long>();

            if (dados.files() == null || dados.files().isEmpty()) {
                for (int i = 0; i < dados.nomePeca().size(); i++) {
                    Optional<Anexo> exists = repository.findById(dados.idsFotos().get(i));

                    Anexo anexo = exists.get();
                    anexo.updateAnexo(
                        dados.idsFotos().get(i),
                        anexo.getNomeFile(),
                        anexo.getIdModelo(),
                        dados.nomePeca().get(i), 
                        dados.qtdPar().get(i),
                        dados.propriedadeFaca().get(i),
                        dados.precoFaca().get(i),
                        dados.obs().get(i)
                    );
    
                    idsAnexos.add(dados.idsFotos().get(i));
                }
            } else {
                for(int i = 0; i < dados.files().size(); i ++) {
                    MultipartFile file = dados.files().get(i).file();
                    String contentType = file.getContentType();

                    String fileName = "";

                    if(!contentType.equals("text/plain")) {
                        if (!directory.exists()) {
                            directory.mkdirs();
                        } 
                        
                        fileName = generateUniqueFileName(dados.files().get(i).originalName(), dados.nomePeca().get(i));
                        
                        String baseName = fileName.substring(0, fileName.indexOf('_'));
                        File[] files = directory.listFiles();
                        
                        for (File existingFile : files) {
                            String fileNamePath = existingFile.getName();
                            String baseNameExistis = fileNamePath.split("_")[0];

                            if (baseNameExistis.equalsIgnoreCase(baseName)) {
                                fileName = existingFile.getName();
                                existingFile.delete();
                                break;
                            }
                        }
        
                        Path targetPath = Paths.get(targetDir, fileName);
                        Files.write(targetPath, file.getBytes());
                        
                    }
                    
                    Long newId = saveAnexoDataBase(
                        dados, 
                        fileName, 
                        dados.nomePeca().get(i), 
                        dados.propriedadeFaca().get(i),
                        dados.qtdPar().get(i),
                        dados.precoFaca().get(i),
                        dados.obs().get(i),
                        dados.idsFotos().get(i)
                    );

                    if(!dados.idsFotos().get(i).equals(newId)) idsAnexos.add(newId);
                }
            }
            return idsAnexos;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo " + e);
        }
    }

    private Long saveAnexoDataBase(
            VariosAnexosDTO dados, 
            String fileName, 
            String nomePecaString, 
            String propriedadeFaca, 
            Long qtdPar,
            Double precoFaca,
            String obs,
            Long id
        ) {
        try {
            Optional<Model> model = modelRepository.findById(dados.idModelo());

            Long idAnexo = null;

            if(id > 0) {
                Optional<Anexo> anexoExists = repository.findById(id);
                Anexo anexo = anexoExists.get();

                idAnexo = id;
                anexo.updateAnexo(id, fileName.isBlank() ? anexo.getNomeFile() : fileName, model.get(), nomePecaString, qtdPar, propriedadeFaca, precoFaca, obs);
                repository.save(anexo);
            } else {
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
                idAnexo = newAnexo.getId();
            }

            return idAnexo;
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
