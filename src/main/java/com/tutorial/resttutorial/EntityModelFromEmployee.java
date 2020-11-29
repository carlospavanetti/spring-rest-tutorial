package com.tutorial.resttutorial;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EntityModelFromEmployee implements RepresentationModelAssembler<Employee, EntityModel<Employee>> {
    @Override
    public EntityModel<Employee> toModel(Employee employee) {
        var selfLink = linkTo( //
                methodOn(EmployeeController.class).one(employee.getId()) //
        ).withSelfRel();
        var rootLink = linkTo( //
                methodOn(EmployeeController.class).all() //
        ).withRel("employees");
        return EntityModel.of(employee, selfLink, rootLink);
    }
}
