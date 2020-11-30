package com.tutorial.resttutorial;

public class OrderNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7356698538463618878L;

    public OrderNotFoundException(Long id) {
        super("Could not find order " + id);
    }
}
