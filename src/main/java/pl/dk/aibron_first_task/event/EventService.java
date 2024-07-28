package pl.dk.aibron_first_task.event;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import pl.dk.aibron_first_task.event.dtos.EventDto;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;
import pl.dk.aibron_first_task.event.event_participants.EventUser;

import java.time.LocalDate;
import java.util.List;

interface EventService {

    EventDto saveEvent(SaveEventDto saveEventDto);

    List<EventDto> getAllEventsForEndDate(LocalDate forDate, int page, int size);

    EventDto findById(Long id);

    void updateEvent(Long id, JsonMergePatch jsonMergePatch);

    void participateInEvent(EventUser eventUser);
}
