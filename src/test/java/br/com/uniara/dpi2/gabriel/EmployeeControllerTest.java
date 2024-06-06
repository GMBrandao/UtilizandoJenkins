package br.com.uniara.dpi2.gabriel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.uniara.dpi2.gabriel.contracts.IEmployeePagingRepository;
import br.com.uniara.dpi2.gabriel.contracts.IEmployeeRepository;
import br.com.uniara.dpi2.gabriel.controller.EmployeeController;
import br.com.uniara.dpi2.gabriel.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

public class EmployeeControllerTest {

    @Mock
    private IEmployeeRepository employeeRepository;

    @Mock
    private IEmployeePagingRepository employeePagingRepository;

    @InjectMocks
    private EmployeeController employeeController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetOneEmployeeExists() {
        Long employeeId = 1L;
        Employee employee = new Employee();
        employee.setId(employeeId);

        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        ResponseEntity<?> response = employeeController.one(employeeId);

        assertEquals(ResponseEntity.ok(employee).getStatusCode(), response.getStatusCode());
        assertEquals(employee, ((Optional<Employee>) response.getBody()).get());
    }

    @Test
    public void testGetOneEmployeeNotExists() {
        Long employeeId = 1L;

        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        ResponseEntity<?> response = employeeController.one(employeeId);

        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testGetAllEmployeesValidPageAndSize() {
        int page = 0;
        int size = 10;
        Employee employee = new Employee();
        Page<Employee> pageEmployees = new PageImpl<>(Collections.singletonList(employee));
        Pageable pageable = PageRequest.of(page, size);

        when(employeePagingRepository.findAll(pageable)).thenReturn(pageEmployees);

        ResponseEntity<?> response = employeeController.all(page, size);

        assertEquals(ResponseEntity.ok(pageEmployees.getContent().toArray()).getStatusCode(), response.getStatusCode());
        assertArrayEquals(pageEmployees.getContent().toArray(), (Object[]) response.getBody());
    }

    @Test
    public void testGetAllEmployeesInvalidPage() {
        int page = -1;
        int size = 10;

        ResponseEntity<?> response = employeeController.all(page, size);

        assertEquals(ResponseEntity.badRequest().body("page deve ser maior ou igual que 0").getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testGetAllEmployeesInvalidSize() {
        int page = 0;
        int size = 501;

        ResponseEntity<?> response = employeeController.all(page, size);

        assertEquals(ResponseEntity.badRequest().body("size deve ser entre 1 e 500").getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testInsertEmployee() {
        Employee employee = new Employee();
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.insert(employee);

        assertEquals(ResponseEntity.ok(employee).getStatusCode(), response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    public void testDeleteEmployeeExists() {
        Long employeeId = 1L;

        when(employeeRepository.existsById(employeeId)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(employeeId);

        ResponseEntity<?> response = employeeController.delete(employeeId);

        assertEquals(ResponseEntity.ok("Employee " + employeeId + " foi excluido com sucesso").getStatusCode(), response.getStatusCode());
    }

    @Test
    public void testDeleteEmployeeNotExists() {
        Long employeeId = 1L;

        when(employeeRepository.existsById(employeeId)).thenReturn(false);

        ResponseEntity<?> response = employeeController.delete(employeeId);

        assertEquals(ResponseEntity.notFound().build().getStatusCode(), response.getStatusCode());
    }
}