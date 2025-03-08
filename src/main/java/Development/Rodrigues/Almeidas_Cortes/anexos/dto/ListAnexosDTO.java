package Development.Rodrigues.Almeidas_Cortes.anexos.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.NotBlank;

public record ListAnexosDTO(
    
    @NotBlank
    MultipartFile file,

    @NotBlank
    String fileName,

    @NotBlank
    String originalName
) {

}
