package pl.dk.aibron_first_task.event;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import pl.dk.aibron_first_task.event.dtos.EventDto;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;
import pl.dk.aibron_first_task.exception.EventExistsException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;
    private EventService underTest;
    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        EventDtoMapper eventDtoMapper = new EventDtoMapper();
        underTest = new EventServiceImpl(eventRepository, eventDtoMapper);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName("It should save new event successfully")
    void itShouldSaveNewEventSuccessfully() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        SaveEventDto saveEventDto = SaveEventDto.builder()
                .name("test name")
                .description("test description")
                .eventStart(now)
                .eventStart(now.plusWeeks(2))
                .price(BigDecimal.valueOf(19.99))
                .build();

        Event savedEvent = Event.builder()
                .id(1L)
                .name("test name")
                .description("test description")
                .eventStart(now)
                .eventStart(now.plusWeeks(2))
                .price(BigDecimal.valueOf(19.99))
                .build();

        when(eventRepository.findByEvent(any(Event.class))).thenReturn(Collections.emptyList());
        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);

        // When
        EventDto eventDto = underTest.saveEvent(saveEventDto);

        // Then
        assertAll(
                () -> Mockito.verify(eventRepository, times(1)).findByEvent(any(Event.class)),
                () -> Mockito.verify(eventRepository, times(1)).save(any(Event.class)),
                () -> assertNotNull(eventDto.id()),
                () -> assertEquals(savedEvent.getName(), eventDto.name()),
                () -> assertEquals(savedEvent.getDescription(), eventDto.description()),
                () -> assertEquals(savedEvent.getPrice(), eventDto.price()),
                () -> assertEquals(savedEvent.getEventStart(), eventDto.eventStart()),
                () -> assertEquals(savedEvent.getEventEnd(), eventDto.eventEnd())
        );
    }

    @Test
    @DisplayName("It should throw exception when user try to save existing event")
    void itShouldThrowExceptionWhenUserTryToSaveExistingEvent() {
        // Given
        LocalDateTime now = LocalDateTime.now();
        SaveEventDto saveEventDto = SaveEventDto.builder()
                .name("test name")
                .description("test description")
                .eventStart(now)
                .eventStart(now.plusWeeks(2))
                .price(BigDecimal.valueOf(19.99))
                .build();

        Event savedEvent = Event.builder()
                .id(1L)
                .name("test name")
                .description("test description")
                .eventStart(now)
                .eventStart(now.plusWeeks(2))
                .price(BigDecimal.valueOf(19.99))
                .build();

        when(eventRepository.findByEvent(any(Event.class))).thenReturn(List.of(savedEvent));

        // When && Then
        assertAll(
                () -> assertThrows(EventExistsException.class, () -> underTest.saveEvent(saveEventDto)),
                () -> Mockito.verify(eventRepository, times(1)).findByEvent(any(Event.class))
        );
    }

    @Test
    void itShouldGetAllEventsForEndDate() {
        // Given
        LocalDate date = LocalDate.now().plusDays(1);
        List<Event> events = ExampleEventResponses.get10ExampleEvents();
        PageImpl<Event> page = new PageImpl<>(events);

        when(eventRepository.findAllByEventEnd(PageRequest.of(0, 25), date)).thenReturn(page);

        // When
        List<EventDto> allEventsForEndDate = underTest.getAllEventsForEndDate(date, 0, 25);

        // Then
        assertAll(
                () -> Mockito.verify(eventRepository, times(1))
                        .findAllByEventEnd(PageRequest.of(0, 25), date),
                () -> assertEquals(page.getSize(), allEventsForEndDate.size())
        );
    }
}