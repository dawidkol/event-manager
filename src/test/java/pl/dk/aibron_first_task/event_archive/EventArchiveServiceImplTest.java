package pl.dk.aibron_first_task.event_archive;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import pl.dk.aibron_first_task.event.EventRepository;
import pl.dk.aibron_first_task.event.ExampleEventResponses;

import static org.mockito.Mockito.*;

class EventArchiveServiceImplTest {

    @Mock
    private EventArchiveRepository eventArchiveRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    private EventArchiveService underTest;
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new EventArchiveServiceImpl(eventRepository, eventArchiveRepository, applicationEventPublisher);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName("test find and mark events as ended")
    void testFindAndMarkEventsAsEnded() {
        // Given
        when(eventRepository.findAllEndedEvents()).thenReturn(ExampleEventResponses.get10ExampleEndedEvents());

        // When
        underTest.findAndMarkEventsAsEnded();

        // Then
        verify(eventRepository, times(1)).findAllEndedEvents();
        verify(applicationEventPublisher, times(1)).publishEvent(anyList());
    }

}