package pl.dk.aibron_first_task.user;

import lombok.Getter;

@Getter
enum Role {
    ADMIN("He's like a Bog"),
    USER("Simple user");

    private String description;

    Role(String description) {
        this.description = description;
    }

}
