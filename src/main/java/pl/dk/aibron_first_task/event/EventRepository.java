package pl.dk.aibron_first_task.event;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query(value = "SELECT * FROM event WHERE event_start >= now() AND event_end <= :localDate", nativeQuery = true)
    Page<Event> findAllByEventEnd(Pageable pageable, @Param("localDate") LocalDate localDate);

    default List<Event> findByEvent(Event event) {
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id");
        Example<Event> example = Example.of(event, matcher);
        return this.findAll(example);
    }

    @Query(value = "SELECT * FROM event WHERE event_end <= now()", nativeQuery = true)
    List<Event> findAllEndedEvents();
}
