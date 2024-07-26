package pl.dk.aibron_first_task.event;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.aibron_first_task.BaseIntegrationTest;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


class EventControllerTest extends BaseIntegrationTest {

    @Test
    void EventControllerIntegrationTest() throws Exception {
        // 1. User wants to create new event with invalid data
        SaveEventDto saveEventDtoInvalid = SaveEventDto.builder()
                .name("Test name")
                .description("Test event description")
                .eventStart(LocalDateTime.now().minusDays(1))
                .eventEnd(LocalDateTime.now().minusDays(23))
                .price(BigDecimal.valueOf(25))
                .build();

        String eventJsonInvalid = objectMapper.writeValueAsString(saveEventDtoInvalid);

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJsonInvalid))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // 2. User wants to create new event with valid data
        SaveEventDto saveEventDtoValid = SaveEventDto.builder()
                .name("Test name")
                .description("Test event description")
                .eventStart(LocalDateTime.now().plusHours(2))
                .eventEnd(LocalDateTime.now().plusHours(3))
                .price(BigDecimal.valueOf(25))
                .build();

        String eventJsonValid = objectMapper.writeValueAsString(saveEventDtoValid);

        mockMvc.perform(MockMvcRequestBuilders.post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJsonValid))
                .andExpect(MockMvcResultMatchers.status().isCreated());


        // 3. User wants to retrieve all events
        LocalDate localDate = LocalDate.now().plusDays(2);
        mockMvc.perform(MockMvcRequestBuilders.get("/events?localDate=%s".formatted(localDate)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(Matchers.greaterThan(0))));
    }

}