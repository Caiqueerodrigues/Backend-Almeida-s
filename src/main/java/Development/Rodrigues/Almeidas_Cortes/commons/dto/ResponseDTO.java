package Development.Rodrigues.Almeidas_Cortes.commons.dto;

public record ResponseDTO (
    Object response,
    String msgErro,
    String msgSucesso,
    String msgAlerta
    
    ) {

}
