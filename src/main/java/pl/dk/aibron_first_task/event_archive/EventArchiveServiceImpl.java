package pl.dk.aibron_first_task.event_archive;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.aibron_first_task.event.Event;
import pl.dk.aibron_first_task.event.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
class EventArchiveServiceImpl implements EventArchiveService {

    private final EventRepository eventRepository;
    private final EventArchiveRepository eventArchiveRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    @Scheduled(cron = "${scheduler.event.archive}")
    public void findAndMarkEventsAsEnded() {
        List<Event> allEndedEvents = eventRepository.findAllEndedEvents();
        if (!allEndedEvents.isEmpty()) {
            applicationEventPublisher.publishEvent(allEndedEvents);
        }
    }

    @EventListener
    @Async
    @Transactional
    void saveEventsInArchive(List<Event> events) {
        List<EventArchive> eventArchiveList = events.stream()
                .map(e -> EventArchive.builder().event(e).build())
                .toList();
        eventArchiveRepository.saveAll(eventArchiveList);
    }
}
