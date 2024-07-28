package pl.dk.aibron_first_task.event.event_participants;

import jakarta.validation.constraints.Positive;

public record EventUser(
        @Positive
        Long eventId,
        @Positive
        Long userId) {
}
