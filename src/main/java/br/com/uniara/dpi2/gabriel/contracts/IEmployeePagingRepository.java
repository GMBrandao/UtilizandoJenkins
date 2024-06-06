package br.com.uniara.dpi2.gabriel.contracts;

import br.com.uniara.dpi2.gabriel.model.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IEmployeePagingRepository extends PagingAndSortingRepository<Employee, Long> {
}