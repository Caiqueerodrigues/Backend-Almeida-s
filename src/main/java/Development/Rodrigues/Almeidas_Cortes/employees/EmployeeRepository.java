package Development.Rodrigues.Almeidas_Cortes.employees;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import Development.Rodrigues.Almeidas_Cortes.employees.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Employee> findAllByIdIn(List<Long> ids);
}
