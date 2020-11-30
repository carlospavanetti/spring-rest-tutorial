package com.tutorial.resttutorial;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class EntityModelFromOrder implements RepresentationModelAssembler<Order, EntityModel<Order>> {

        @Override
        public EntityModel<Order> toModel(Order order) {
                var self = methodOn(OrderController.class).one(order.getId());
                var root = methodOn(OrderController.class).all();
                var model = EntityModel.of(order, //
                                linkTo(self).withSelfRel(), //
                                linkTo(root).withRel("orders"));

                if (order.getStatus() == Status.IN_PROGRESS) {
                        var cancel = methodOn(OrderController.class).cancel(order.getId());
                        model.add(linkTo(cancel).withRel("cancel"));
                        var complete = methodOn(OrderController.class).complete(order.getId());
                        model.add(linkTo(complete).withRel("complete"));
                }

                return model;
        }
}
