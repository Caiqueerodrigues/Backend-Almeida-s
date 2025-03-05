package Development.Rodrigues.Almeidas_Cortes.anexos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import Development.Rodrigues.Almeidas_Cortes.anexos.dto.AnexoDTO;
import Development.Rodrigues.Almeidas_Cortes.anexos.dto.ListAnexosDTO;
import Development.Rodrigues.Almeidas_Cortes.anexos.entities.Anexo;
import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.models.ModelRepository;
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

    public ResponseDTO insertImageModelService(@Valid AnexoDTO dados) {
        try {
            String targetDir = uploadDir + File.separator + dados.nomeModelo();

            saveFileToDisk(dados, targetDir);

            return new ResponseDTO("", "", "Foto(s) anexada(s) com sucecsso!", "");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo", e);
        }
    }

    public Resource getPhotoService(String model, String file) {
        try {
            String targetDir = uploadDir + File.separator + model + File.separator + file;
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

    private void saveFileToDisk(AnexoDTO dados, String targetDir) throws IOException {
        try {
            
            File directory = new File(targetDir);
            
            for(int i = 0; i < dados.files().size(); i ++) {
                boolean newImage = true;
                String fileName = generateUniqueFileName(dados.files().get(i).originalName(), dados.nomePeca().get(i));
                
                MultipartFile file = dados.files().get(i).files();


                if (!directory.exists()) {
                    directory.mkdirs();
                } else {
                    String baseName = fileName.substring(0, fileName.indexOf('_'));
                    File[] files = directory.listFiles();
                    
                    for (File existingFile : files) {
                        if (existingFile.getName().startsWith(baseName)) {
                            fileName = existingFile.getName();
                            existingFile.delete();
                            newImage = false;
                            break;
                        }
                    }
                }

                Path targetPath = Paths.get(targetDir, fileName);
                Files.write(targetPath, file.getBytes());
                if(newImage) saveAnexoDataBase(dados, fileName, dados.nomePeca().get(i));
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar o arquivo", e);
        }
    }

    private void saveAnexoDataBase(AnexoDTO dados, String fileName, String nomePecaString) {
        try {
            Optional<Model> model = modelRepository.findById(dados.idModelo());

            Anexo newAnexo = new Anexo(
                fileName,
                model.get(),
                nomePecaString,
                dados.pecaPar(),
                dados.propriedadeFaca(),
                dados.precoFaca(),
                dados.obs()
            );

            repository.save(newAnexo);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o arquivo", e);
        }
    }

    private String generateUniqueFileName(String originalFileName, String nomePeca) {
        String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
        String uniqueFileName = nomePeca + "_" + UUID.randomUUID().toString() + extension;
        return uniqueFileName;
    }
}
