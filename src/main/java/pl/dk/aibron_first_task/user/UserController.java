package pl.dk.aibron_first_task.user;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.dk.aibron_first_task.user.dtos.RegistrationUserDto;
import pl.dk.aibron_first_task.user.dtos.UserDto;

import java.net.URI;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegistrationUserDto registrationUserDto) {
        UserDto userDto = userService.saveUser(registrationUserDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("{/id}")
                .buildAndExpand(userDto.id())
                .toUri();
        return ResponseEntity.created(uri).body(userDto);
    }
}
