package pl.dk.aibron_first_task.event;

import pl.dk.aibron_first_task.event.dtos.EventDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ExampleEventResponses {

    static Random r = new Random();
    static double random = r.nextDouble();

    static List<Event> get10ExampleEvents() {
        List<Event> events = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            Event event = Event.builder()
                    .id((long) i)
                    .name("Event " + i)
                    .description("Description for Event " + i)
                    .eventStart(LocalDateTime.now().plusDays(i))
                    .eventEnd(LocalDateTime.now().plusDays(i).plusHours(2))
                    .price(BigDecimal.valueOf(random + i))
                    .build();
            events.add(event);
        }
        return events;
    }

    static List<EventDto> get10ExampleEventDtos() {
        List<EventDto> events = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            EventDto event = EventDto.builder()
                    .id((long) i)
                    .name("Event " + i)
                    .description("Description for Event " + i)
                    .eventStart(LocalDateTime.now().plusDays(i))
                    .eventEnd(LocalDateTime.now().plusDays(i).plusHours(2))
                    .price(BigDecimal.valueOf(random + i))
                    .build();
            events.add(event);
        }
        return events;
    }


}
