package pl.dk.aibron_first_task.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;
    private EventService underTest;
    private AutoCloseable autoCloseable;
    private com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        EventDtoMapper eventDtoMapper = new EventDtoMapper();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        underTest = new EventServiceImpl(eventRepository, eventDtoMapper, objectMapper);
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
    @DisplayName("It should get all Events for end date")
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

    @Test
    @DisplayName("It should find Event by given id")
    void itShouldFindEventByGivenId() throws JsonProcessingException {
        // Given
        Long eventId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Event event = Event.builder()
                .id(1L)
                .name("test name")
                .description("test description")
                .eventStart(now)
                .eventStart(now.plusWeeks(2))
                .price(BigDecimal.valueOf(19.99))
                .build();

        Event updatedEvent = Event.builder()
                .id(1L)
                .name("test name - updated")
                .description("test description - updated")
                .eventStart(now)
                .eventStart(now.plusWeeks(2))
                .price(BigDecimal.valueOf(19.99))
                .build();


        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);
        ArgumentCaptor<Event> eventArgumentCaptor = ArgumentCaptor.forClass(Event.class);

        String jsonMergePatchUpdate = """
                {
                    "name": "test name - updated",
                    "description": "test description - updated"
                }
                """.trim();

        JsonMergePatch jsonMergePatch = objectMapper.readValue(jsonMergePatchUpdate, JsonMergePatch.class);

        // When
        underTest.updateEvent(eventId, jsonMergePatch);

        // Then
        assertAll(
                () -> verify(eventRepository, times(1)).findById(eventId),
                () -> verify(eventRepository, times(1)).save(eventArgumentCaptor.capture()),
                () -> assertEquals("test name - updated", eventArgumentCaptor.getValue().getName()),
                () -> assertEquals("test description - updated", eventArgumentCaptor.getValue().getDescription())
        );
    }

    @Test
    @DisplayName("It should return Event by given id")
    void itShouldReturnEvenByGivenId() {
        // Given
        Long eventId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Event event = Event.builder()
                .id(eventId)
                .name("test name")
                .description("test description")
                .eventStart(now)
                .eventStart(now.plusWeeks(2))
                .price(BigDecimal.valueOf(19.99))
                .build();

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        // When
        EventDto eventDtoByID = underTest.findById(eventId);

        // Then
        assertAll(
                () -> assertEquals(event.getId(), eventDtoByID.id()),
                () -> assertEquals(event.getName(), eventDtoByID.name()),
                () -> assertEquals(event.getDescription(), eventDtoByID.description()),
                () -> assertEquals(event.getPrice(), eventDtoByID.price()),
                () -> assertEquals(event.getEventStart(), eventDtoByID.eventStart()),
                () -> assertEquals(event.getEventEnd(), eventDtoByID.eventEnd())
        );
    }
}