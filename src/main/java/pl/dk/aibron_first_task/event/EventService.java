package pl.dk.aibron_first_task.event;

import pl.dk.aibron_first_task.event.dtos.EventDto;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;

import java.time.LocalDate;
import java.util.List;

interface EventService {

    EventDto saveEvent(SaveEventDto saveEventDto);

    List<EventDto> getAllEventsForEndDate(LocalDate forDate, int page, int size);
}
