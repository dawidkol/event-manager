package pl.dk.aibron_first_task.user.dtos;

import lombok.Builder;

@Builder
public record UserDto(
        Long id,
        String firstName,
        String lastName,
        String email
) {
}
