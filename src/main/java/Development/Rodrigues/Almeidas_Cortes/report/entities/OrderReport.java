package Development.Rodrigues.Almeidas_Cortes.report.entities;

import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.report.dto.OrderReportDTO;
import jakarta.persistence.Id;

public class OrderReport {
    private Long id;
    
    private String nomeModelo;
    
    private String color;
    
    private Long qtdPecas;
    
    private String nameclient;

    private Double totalDinheiro;

    private String dataPedido;

    private String diaSemana;

    private Object grade;

    private Long totalPecas;

    private String quemAssinou;

    private String dataRetirada;

    private String horaRetirada;
    
    public OrderReport( OrderReportDTO dados) {
        this.id = dados.id();
        this.nomeModelo = dados.nomeModelo();
        this.color = dados.color();
        this.qtdPecas = dados.qtdPecas();
        this.nameclient = dados.nameclient();
        this.totalDinheiro = dados.totalDinheiro();
        this.dataPedido = dados.dataPedido();
        this.diaSemana = dados.diaSemana();
        this.grade = dados.grade();
        this.totalPecas = dados.totalPecas();
        this.quemAssinou = dados.quemAssinou();
        this.dataRetirada = dados.dataRetirada();
        this.horaRetirada = dados.horaRetirada();
    }
}
