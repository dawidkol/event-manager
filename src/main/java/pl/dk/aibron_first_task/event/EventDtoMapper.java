package pl.dk.aibron_first_task.event;

import org.springframework.stereotype.Service;
import pl.dk.aibron_first_task.event.dtos.EventDto;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;

@Service
class EventDtoMapper {

    public Event map(SaveEventDto saveEventDto) {
        return Event.builder()
                .name(saveEventDto.name())
                .description(saveEventDto.description())
                .eventStart(saveEventDto.eventStart())
                .eventEnd(saveEventDto.eventEnd())
                .price(saveEventDto.price())
                .build();
    }

    public EventDto map(Event event) {
        return EventDto.builder()
                .id(event.getId())
                .name(event.getName())
                .description(event.getDescription())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .price(event.getPrice())
                .build();
    }

    public SaveEventDto mapToUpdate(Event event) {
        return SaveEventDto.builder()
                .name(event.getName())
                .description(event.getDescription())
                .eventStart(event.getEventStart())
                .eventEnd(event.getEventEnd())
                .price(event.getPrice())
                .build();
    }
}
