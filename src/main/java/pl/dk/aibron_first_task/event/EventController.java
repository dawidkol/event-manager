package pl.dk.aibron_first_task.event;

import com.github.fge.jsonpatch.mergepatch.JsonMergePatch;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.aibron_first_task.constants.PaginationConstants;
import pl.dk.aibron_first_task.event.dtos.EventDto;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
@Validated
class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventDto> createEvent(@Valid @RequestBody SaveEventDto saveEventDto) {
        EventDto savedEvent = eventService.saveEvent(saveEventDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedEvent.id())
                .toUri();
        return ResponseEntity.created(uri).body(savedEvent);
    }

    @GetMapping
    public ResponseEntity<List<EventDto>> getEventsByEndDate(
            @Future @RequestParam(required = true) LocalDate localDate,
            @PositiveOrZero @RequestParam(required = false, defaultValue = PaginationConstants.PAGE_DEFAULT) int page,
            @PositiveOrZero @RequestParam(required = false, defaultValue = PaginationConstants.PAGE_SIZE_DEFAULT) int size) {
        List<EventDto> allEventsForEndDate = eventService.getAllEventsForEndDate(localDate, page, size);
        return ResponseEntity.ok(allEventsForEndDate);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@Positive @PathVariable Long id) {
        EventDto eventById = eventService.findById(id);
        return ResponseEntity.ok(eventById);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateEvent(@Positive @PathVariable Long id, @RequestBody JsonMergePatch jsonMergePatch) {
        eventService.updateEvent(id, jsonMergePatch);
        return ResponseEntity.noContent().build();
    }

}
