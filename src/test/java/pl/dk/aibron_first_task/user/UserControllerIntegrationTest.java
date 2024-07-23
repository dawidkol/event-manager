package pl.dk.aibron_first_task.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.dk.aibron_first_task.BaseIntegrationTest;


class UserControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void userControllerIntegrationTest() throws Exception {
        // 1. User wants to register with invalid data [password to short]. HTTP STATUS = 400
        String registrationUserDtoJsonWithInvalidPassword = """
                {
                	"firstName": "Add your name in the body",
                    "lastName": "Add your name in the body",
                    "email": "john@doe.com",
                    "password": "pass"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationUserDtoJsonWithInvalidPassword))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        // 2. User wants to register with valid data. HTTP STATUS = 401
        String registrationUserDtoJsonWithValidData = """
                {
                	"firstName": "Add your name in the body",
                    "lastName": "Add your name in the body",
                    "email": "john@doe.com",
                    "password": "password"
                }
                """;
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationUserDtoJsonWithValidData))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        // 3. User wants to register same user one more time. HTTP STATUS = 409
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registrationUserDtoJsonWithValidData))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

}
