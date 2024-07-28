package pl.dk.aibron_first_task.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegistrationUserDto(
        @NotBlank
        @Size(min = 3, max = 50)
        String firstName,
        @NotBlank
        @Size(min = 3, max = 50)
        String lastName,
        @NotBlank
        @Size(min = 6, max = 200)
        String password,
        @Email
        String email,
        @Pattern(regexp = "^[+][0-9]{1,3}[0-9]{10,14}$")
        String phoneNumber
) {

}
