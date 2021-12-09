package org.bstu.govoronok.model;

public enum Role {
    ADMINISTRATOR("ADMINISTRATOR"),
    USER("USER");

    private final String role;

    private Role(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
