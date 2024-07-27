package pl.dk.aibron_first_task.user;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.dk.aibron_first_task.exception.UserExistsException;
import pl.dk.aibron_first_task.user.dtos.RegistrationUserDto;
import pl.dk.aibron_first_task.user.dtos.UserDto;

import java.util.Optional;

@Service
@AllArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserDtoMapper userDtoMapper;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserDto saveUser(RegistrationUserDto registrationUserDto) {
        validUserEmail(registrationUserDto);
        User userToSave = setUserProperties(registrationUserDto);
        User savedUser = userRepository.save(userToSave);
        return userDtoMapper.map(savedUser);
    }

    private User setUserProperties(RegistrationUserDto registrationUserDto) {
        User userToSave = userDtoMapper.map(registrationUserDto);
        String encodedPassword = passwordEncoder.encode(userToSave.getPassword());
        userToSave.setPassword(encodedPassword);
        String role = Role.USER.name();
        userRoleRepository.findByName(role)
                .ifPresent(userToSave::setUserRole);
        return userToSave;
    }

    private void validUserEmail(RegistrationUserDto registrationUserDto) {
        Optional<User> optionalUser = userRepository.findByEmail(registrationUserDto.email());
        if (optionalUser.isPresent()) {
            String email = optionalUser.get().getEmail();
            throw new UserExistsException("User with email %s exists".formatted(email));
        }
    }

}
