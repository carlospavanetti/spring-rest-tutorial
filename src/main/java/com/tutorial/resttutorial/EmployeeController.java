package com.tutorial.resttutorial;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private EntityModelFromEmployee conversion;

    @GetMapping("/employees")
    CollectionModel<EntityModel<Employee>> all() {
        var employees = repository.findAll().stream() //
                .map(conversion::toModel) //
                .collect(Collectors.toList());
        return CollectionModel.of(employees, //
                linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
    }

    @PostMapping("/employees")
    ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {
        var model = conversion.toModel(repository.save(newEmployee));
        var selfUri = model.getRequiredLink(IanaLinkRelations.SELF).toUri();
        return ResponseEntity.created(selfUri).body(model);
    }

    @GetMapping("/employees/{id}")
    EntityModel<Employee> one(@PathVariable Long id) {
        var employee = repository.findById(id) //
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        return conversion.toModel(employee);
    }

    @PutMapping("/employee/{id}")
    Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
        return repository.findById(id).map(employee -> {
            employee.setName(newEmployee.getName());
            employee.setRole(newEmployee.getRole());
            return repository.save(employee);
        }).orElseGet(() -> {
            newEmployee.setId(id);
            return repository.save(newEmployee);
        });
    }

    @DeleteMapping("/employee/{id}")
    ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
