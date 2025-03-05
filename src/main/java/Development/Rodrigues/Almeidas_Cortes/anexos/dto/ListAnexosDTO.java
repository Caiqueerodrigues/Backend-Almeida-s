package Development.Rodrigues.Almeidas_Cortes.anexos.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public record ListAnexosDTO(
    
    @NotBlank
    MultipartFile files,

    @NotBlank
    String fileName,

    @NotBlank
    String originalName
) {

}
