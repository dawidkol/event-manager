package pl.dk.aibron_first_task.event.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record EventDto(
        Long id,
        String name,
        String description,
        LocalDateTime eventStart,
        LocalDateTime eventEnd,
        BigDecimal price
) {
}
