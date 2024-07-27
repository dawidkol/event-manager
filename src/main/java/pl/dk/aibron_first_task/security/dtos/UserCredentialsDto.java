package pl.dk.aibron_first_task.security.dtos;

import lombok.Builder;

@Builder
public record UserCredentialsDto(String email, String password, String role) {
}
