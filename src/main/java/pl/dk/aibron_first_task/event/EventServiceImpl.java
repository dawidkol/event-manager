package pl.dk.aibron_first_task.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.aibron_first_task.event.dtos.EventDto;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;
import pl.dk.aibron_first_task.exception.EventExistsException;
import pl.dk.aibron_first_task.exception.EventNotFoundException;
import pl.dk.aibron_first_task.exception.ServerException;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventDtoMapper eventDtoMapper;
    private final ObjectMapper objectMapper;

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

    @Override
    public EventDto findById(Long id) {
        return eventRepository.findById(id)
                .map(eventDtoMapper::map)
                .orElseThrow(() -> new EventNotFoundException("Event with id %s not found".formatted(id)));
    }

    @Override
    @Transactional
    public void updateEvent(Long id, JsonMergePatch jsonMergePatch) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EventNotFoundException("Event with id %s not found".formatted(id)));
        SaveEventDto dto = eventDtoMapper.mapToUpdate(event);
        try {
            SaveEventDto saveEventDto = this.applyPatch(dto, jsonMergePatch);
            Event eventWithUpdateProperties = eventDtoMapper.map(saveEventDto);
            eventWithUpdateProperties.setId(id);
            eventRepository.save(eventWithUpdateProperties);
        } catch (JsonPatchException | JsonProcessingException e) {
            throw new ServerException();
        }
    }

    private SaveEventDto applyPatch(SaveEventDto saveEventDto, JsonMergePatch jsonMergePatch) throws JsonPatchException, JsonProcessingException {
        JsonNode jsonNode = objectMapper.valueToTree(saveEventDto);
        JsonNode apply = jsonMergePatch.apply(jsonNode);
        return objectMapper.treeToValue(apply, SaveEventDto.class);
    }

}
