package pl.dk.aibron_first_task.event;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.aibron_first_task.event.dtos.EventDto;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;
import pl.dk.aibron_first_task.exception.EventExistsException;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventDtoMapper eventDtoMapper;

    @Override
    @Transactional
    public EventDto saveEvent(SaveEventDto saveEventDto) {
        Event eventToSave = eventDtoMapper.map(saveEventDto);
        if (!eventRepository.findByEvent(eventToSave).isEmpty()) {
            throw new EventExistsException("Event you try to save already exists. Check your data");
        }
        Event savedEvent = eventRepository.save(eventToSave);
        return eventDtoMapper.map(savedEvent);
    }

    @Override
    public List<EventDto> getAllEventsForEndDate(LocalDate forDate, int page, int size) {
        return eventRepository.findAllByEventEnd(PageRequest.of(page, size), forDate)
                .stream()
                .map(eventDtoMapper::map)
                .toList();
    }

}
