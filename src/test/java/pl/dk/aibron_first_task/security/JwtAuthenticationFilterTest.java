package pl.dk.aibron_first_task.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.aibron_first_task.BaseIntegrationTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtAuthenticationFilterTest extends BaseIntegrationTest {

    @Test
    @Sql("/test-jwt-authentication-filter.sql")
    @DisplayName("It should test authentication endpoint")
    void testAuthenticationEndpoint() throws Exception {
        // 1. User wants to get token but he's not registered
        String nonRegisteredUser = """
                {
                    "email": "tomasz.kowalski@test.pl",
                    "password": "password"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/auth").content(nonRegisteredUser).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());

        // 2. Registered user wants to get token
        String user = """
                {
                    "email": "john.doe@test.pl",
                    "password": "securePassword123"
                }
                """;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/auth").content(user).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("token"));
    }
}