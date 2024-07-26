package pl.dk.aibron_first_task.event;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import pl.dk.aibron_first_task.user.User;
import pl.dk.aibron_first_task.validators.event.EventDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "event")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EventDate
@Builder
@EqualsAndHashCode(exclude = {"id"})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 5, max = 30)
    private String name;
    @NotBlank
    @Size(min = 10, max = 200)
    private String description;
    @NotNull
    @FutureOrPresent
    private LocalDateTime eventStart;
    @NotNull
    @FutureOrPresent
    private LocalDateTime eventEnd;
    @PositiveOrZero
    private BigDecimal price;
    @OneToMany
    @JoinTable(name = "event_user", joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();
}
