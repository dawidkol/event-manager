package pl.dk.aibron_first_task.user;

import pl.dk.aibron_first_task.user.dtos.RegistrationUserDto;
import pl.dk.aibron_first_task.user.dtos.UserDto;

interface UserService {

    UserDto saveUser(RegistrationUserDto registrationUserDto);

}
