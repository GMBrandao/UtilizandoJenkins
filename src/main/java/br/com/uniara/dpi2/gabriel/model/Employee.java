package br.com.uniara.dpi2.gabriel.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Employee {
    public Employee(String name, String role){
        this.name = name;
        this.role = role;
    }

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String role;
}
