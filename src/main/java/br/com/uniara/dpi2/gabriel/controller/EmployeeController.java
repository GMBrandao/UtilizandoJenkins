package br.com.uniara.dpi2.gabriel.controller;

import br.com.uniara.dpi2.gabriel.contracts.IEmployeePagingRepository;
import br.com.uniara.dpi2.gabriel.contracts.IEmployeeRepository;
import br.com.uniara.dpi2.gabriel.model.Employee;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    IEmployeeRepository employeeRepository;

    @Autowired
    IEmployeePagingRepository employeePagingRepository;

    @GetMapping("/{id}")
    public ResponseEntity<?> one(@PathVariable Long id)
    {
        if(employeeRepository.existsById(id)){
            return ResponseEntity.ok(employeeRepository.findById(id));
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<?> all(
        @RequestParam int page,
        @RequestParam int size)
    {
        if (page < 0) {
           return ResponseEntity.badRequest()
                   .body("page deve ser maior ou igual que 0");
        }

        if (size < 1 || size > 500){
            return ResponseEntity.badRequest()
                    .body("size deve ser entre 1 e 500");
        }

        Pageable pageable = PageRequest.of(page, size);

        final Page<Employee> listEmployee = employeePagingRepository.findAll(pageable);

        return ResponseEntity.ok(listEmployee.stream().toArray());
    }

    @PostMapping
    public ResponseEntity<Employee> insert (
            @RequestBody Employee employee)
    {
        val employeeSalvo = employeeRepository.save(employee);

        return ResponseEntity.ok(employeeSalvo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id)
    {
        if(employeeRepository.existsById(id)){
            employeeRepository.deleteById(id);
            return ResponseEntity.ok("Employee " + id + " foi excluido com sucesso");
        }

        return ResponseEntity.notFound().build();
    }
}
