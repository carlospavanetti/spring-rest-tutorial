package com.tutorial.resttutorial;

public class EmployeeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 7858702281068055805L;

    public EmployeeNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
