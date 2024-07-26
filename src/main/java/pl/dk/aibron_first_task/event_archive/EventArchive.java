package pl.dk.aibron_first_task.event_archive;

import jakarta.persistence.*;
import lombok.Builder;
import pl.dk.aibron_first_task.event.Event;

@Entity
@Table(name = "event_archive")
@Builder
record EventArchive(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id,
        @OneToOne
        @JoinColumn(name = "event_id", unique = true)
        Event event

) {

}
