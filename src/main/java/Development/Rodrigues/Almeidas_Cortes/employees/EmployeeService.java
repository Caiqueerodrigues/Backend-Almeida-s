package Development.Rodrigues.Almeidas_Cortes.employees;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import Development.Rodrigues.Almeidas_Cortes.commons.dto.ResponseDTO;
import Development.Rodrigues.Almeidas_Cortes.employees.dto.CreateEmployeeDTO;
import Development.Rodrigues.Almeidas_Cortes.employees.dto.UpdateEmployeeDTO;
import Development.Rodrigues.Almeidas_Cortes.employees.entities.Employee;
import Development.Rodrigues.Almeidas_Cortes.employees.entities.EmployeeFront;
import Development.Rodrigues.Almeidas_Cortes.historyOrders.HistoryOrderService;
import Development.Rodrigues.Almeidas_Cortes.services.GetDateHourBrasilia;
import Development.Rodrigues.Almeidas_Cortes.users.entities.User;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository repository;

    @Autowired
    GetDateHourBrasilia dataHoraBrasilia;

    private static final Logger log = LoggerFactory.getLogger(HistoryOrderService.class);

    public ResponseDTO getAllDateService( LocalDate dateIinicial, LocalDate dateFinal) {
        try {
            List<Employee> employees = repository.findAllByDateBetweenOrderByDateAsc(dateIinicial, dateFinal);

            if (employees != null && !employees.isEmpty()) {
                List<EmployeeFront> employeeDTOs = employees.stream()
                    .map(emp ->  {
                        String[] horariosArray = new String[0];
                        if (emp.getHorarios() != null && !emp.getHorarios().isBlank()) {
                            horariosArray = emp.getHorarios().split(",\\s*"); 
                        }

                        return new EmployeeFront(
                            emp.getId(),
                            emp.getDate(),
                            horariosArray,
                            emp.getNomeFuncionario(),
                            emp.isStatus()
                        );
                    })
                    .collect(Collectors.toList());
                return new ResponseDTO(employeeDTOs, "", "", "");
            }
            return new ResponseDTO("", "", "", "Nenhum dado encontrado para este período!");
        } catch (Exception e) {
            log.error("ERRO ao obter os dados " + e);
            throw new RuntimeException("Erro ao obter os dados, tente novamente.");
        }
    }

    public ResponseDTO getIdService(Long id) {
        try {
            Optional<Employee> register = repository.findById(id);

            if(register.isPresent()) {
                Employee emp = register.get();
                String[] horariosArray = new String[0];
                if (emp.getHorarios() != null && !emp.getHorarios().isBlank()) {
                    horariosArray = emp.getHorarios().split(",\\s*"); 
                }

                EmployeeFront employeeFront = new EmployeeFront(
                    emp.getId(),
                    emp.getDate(),
                    horariosArray,
                    emp.getNomeFuncionario(),
                    emp.isStatus()
                );
                return new ResponseDTO(employeeFront, "", "", "");
            } else {
                return new ResponseDTO("", "", "", "Dados informados inválidos!");
            }
        } catch (Exception e) {
            log.error("ERRO ao obter os dados " + e);
            throw new RuntimeException("Erro ao obter os dados, tente novamente.");
        }
    }

    public ResponseDTO createRegisterService(CreateEmployeeDTO dados) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        try {
            Employee newRegister = new Employee(dados, user, dataHoraBrasilia.dataHoraBrasiliaLocalDateTime());
            repository.save(newRegister);

            return new ResponseDTO("", "", "Dados salvos com sucesso!", "");
        } catch (Exception e) {
            log.error("ERRO ao salvar os dados " + e);
            throw new RuntimeException("Erro ao salvar os dados, tente novamente.");
        }
    }

    public ResponseDTO updateRegisterService(UpdateEmployeeDTO dados) {
        try {
            Optional<Employee> register = repository.findById(dados.id());

            if(register.isPresent()) {
                Employee exists = register.get();
                exists.updateEmployee(
                    dataHoraBrasilia.dataHoraBrasiliaLocalDateTime(),
                    dados.date(),
                    dados.horarios(),
                    dados.nomeFuncionario(),
                    dados.status()
                );

                repository.save(exists);
                return new ResponseDTO("", "", "Dados salvos com sucesso!", "");
            } else {
                return new ResponseDTO("", "", "", "Funcionário não encontrado!");
            }
        } catch (Exception e) {
            log.error("ERRO ao salvar os dados " + e);
            throw new RuntimeException("Erro ao salvar os dados, tente novamente.");
        }
    }

    public ResponseDTO updateStatusPaymentService(List<Long> ids) {
        try {
            List<Employee> employees = repository.findAllByIdIn(ids);

            if (employees.isEmpty()) {
                return new ResponseDTO("", "", "", "Nenhum dado encontrado com os IDs fornecidos.");
            }

            for (Employee employee : employees) {
                employee.setStatus(true);
                repository.save(employee);
            }

            return new ResponseDTO("", "", "Status de pagamento atualizado com sucesso!", "");
        } catch (Exception e) {
            log.error("Erro ao atualizar os status de pagamento: ", e);
            return new ResponseDTO("", "Erro ao atualizar os dados, tente novamente.", "", "");
        }
    }
}
