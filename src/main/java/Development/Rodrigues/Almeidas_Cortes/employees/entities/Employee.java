package Development.Rodrigues.Almeidas_Cortes.employees.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import Development.Rodrigues.Almeidas_Cortes.commons.services.ObterDataHoraService;
import Development.Rodrigues.Almeidas_Cortes.employees.dto.CreateEmployeeDTO;
import Development.Rodrigues.Almeidas_Cortes.employees.dto.UpdateEmployeeDTO;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "tab_employees")
@Entity(name = "Employee")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;

    @Column(name = "nome_funcionario", nullable = false)
    private String nomeFuncionario;

    @Column(name = "data", nullable = false)
    private LocalDate date;

    @Column(name = "horarios", nullable = false)
    private String horarios;

    @Column(name = "status_Pagamento")
    private boolean status;

    @Column(name = "update_AT", nullable = false)
    private LocalDateTime update_AT;

    @ManyToOne
    @JoinColumn(name = "id_User", nullable = false)
    private User user;

    public Employee(CreateEmployeeDTO dados, User dataUser, LocalDateTime update_AT) {
        this.dataRegistro = update_AT;
        this.date = dados.date();
        this.horarios = dados.horarios();
        this.user = dataUser;
        this.nomeFuncionario = dados.nomeFuncionario();
        this.status = false;
    }

    public void updateEmployee(
        LocalDateTime update_AT,
        LocalDate date,
        String horarios,
        String nomeFuncionario,
        boolean status
    ) {
        this.update_AT = update_AT;
        this.date = date;
        this.horarios = horarios;
        this.nomeFuncionario = nomeFuncionario;
        this.status = status;
    }
}