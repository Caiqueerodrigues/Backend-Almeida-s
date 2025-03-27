package Development.Rodrigues.Almeidas_Cortes.report.dto;

public record OrderReportDTO(
    Long id,
        
    String nomeModelo,
    
    String color,
    
    Long qtdPecas,
    
    String nameclient,

    Double totalDinheiro,

    String dataPedido,

    String diaSemana,

    Object grade,

    Long totalPecas,

    String quemAssinou,

    String dataRetirada,

    String horaRetirada
) {

}
