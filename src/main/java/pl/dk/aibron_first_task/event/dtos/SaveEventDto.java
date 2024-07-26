package pl.dk.aibron_first_task.event.dtos;

import jakarta.validation.constraints.*;
import lombok.Builder;
import pl.dk.aibron_first_task.validators.event.EventDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@EventDate
public record SaveEventDto(
        @NotBlank
        @Size(min = 5, max = 30)
        String name,
        @NotBlank
        @Size(min = 10, max = 200)
        String description,
        @NotNull
        @FutureOrPresent
        LocalDateTime eventStart,
        @NotNull
        @FutureOrPresent
        LocalDateTime eventEnd,
        @PositiveOrZero
        BigDecimal price
) {
}
