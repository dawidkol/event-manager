package pl.dk.aibron_first_task.user;

import org.springframework.stereotype.Service;
import pl.dk.aibron_first_task.user.dtos.RegistrationUserDto;
import pl.dk.aibron_first_task.user.dtos.UserDto;

@Service
class UserDtoMapper {

    public UserDto map(User user) {
        return UserDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .build();
    }

    public User map(RegistrationUserDto registrationUserDto) {
        return User.builder()
                .firstName(registrationUserDto.firstName())
                .lastName(registrationUserDto.lastName())
                .email(registrationUserDto.email())
                .password(registrationUserDto.password())
                .build();
    }
}
