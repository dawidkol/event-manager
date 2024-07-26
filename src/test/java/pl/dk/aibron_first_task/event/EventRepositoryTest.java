package pl.dk.aibron_first_task.event;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventRepositoryTest {

    @Autowired
    private EventRepository underTest;

    @Test
    void itShouldReturn10Records() {
        // Given
        LocalDate localDateTime = LocalDate.now().plusYears(1);
        List<Event> events = ExampleEventResponses.get10ExampleEvents();
        underTest.saveAll(events);

        // When
        Page<Event> allByEventEnd = underTest.findAllByEventEnd(PageRequest.of(0, 25), localDateTime);
        long totalElements = allByEventEnd.getTotalElements();

        // Then
        assertEquals(10, totalElements);
    }
}