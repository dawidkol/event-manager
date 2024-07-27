package pl.dk.aibron_first_task.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.aibron_first_task.BaseIntegrationTest;
import pl.dk.aibron_first_task.user.UserRepository;

import java.io.IOException;

import static org.mockito.Mockito.mock;

class BearerTokenFilterTest extends BaseIntegrationTest {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    @DisplayName("It should retrieve event by given id")
    @Sql(value = {"/test-jwt-authentication-filter.sql", "/test-event-repository.sql"})
    void itShouldRetrieveEventByGivenId() throws Exception {
        // Given
        String user = """
                {
                    "email": "john.doe@test.pl",
                    "password": "securePassword123"
                }
                """;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                        .content(user)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String stringResult = result.getResponse().getContentAsString();
        Wrapper wrapper = objectMapper.readValue(stringResult, Wrapper.class);
        logger.warn(wrapper.token);

//        mockMvc.perform(MockMvcRequestBuilders.get("/events/1")
//                        .header("Authorization", "Bearer " + wrapper.token))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("It should throw parse Exception")
    void itShouldThrowParseException() throws ServletException, IOException {
        // Given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        String sharedKeyForTests = "8e55772b-26c5-4114-bbe9-cb6d44af2ce4";
        UserRepository userRepositoryMock = mock(UserRepository.class);
        BearerTokenFilter bearerTokenFilter = new BearerTokenFilter(new JwtService(sharedKeyForTests, userRepositoryMock));
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer invalidToken");

        // When
        bearerTokenFilter.doFilter(request, response, filterChain);
    }

    private record Wrapper(String username, String token) {
    }
}