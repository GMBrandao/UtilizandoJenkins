package br.com.uniara.dpi2.gabriel.contracts;

import br.com.uniara.dpi2.gabriel.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEmployeeRepository extends JpaRepository<Employee, Long> {
}