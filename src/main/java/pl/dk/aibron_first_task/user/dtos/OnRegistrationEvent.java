package pl.dk.aibron_first_task.user.dtos;

import lombok.Builder;

@Builder
public record OnRegistrationEvent(String firstName, String phoneNumber) {
}
