package pl.dk.aibron_first_task.user;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.dk.aibron_first_task.exception.UserExistsException;
import pl.dk.aibron_first_task.user.dtos.RegistrationUserDto;
import pl.dk.aibron_first_task.user.dtos.UserDto;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRoleRepository userRoleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserService underTest;
    @Mock
    private PasswordEncoder passwordEncoder;

    private AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        UserDtoMapper userDtoMapper = new UserDtoMapper();
        underTest = new UserServiceImpl(userRepository, userDtoMapper, userRoleRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @DisplayName("It should save new user with success")
    void itShouldSaveNewUserWithSuccess() {
        // Given
        RegistrationUserDto userToSave = RegistrationUserDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.pl")
                .password("verySecretPassword")
                .build();

        long id = 1L;
        UserRole userRole = UserRole.builder()
                .id(id)
                .name(Role.ADMIN.name())
                .description(Role.ADMIN.getDescription())
                .build();

        User user = User.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.pl")
                .password("verySecretPassword")
                .userRole(userRole)
                .build();

        when(userRepository.findByEmail(userToSave.email())).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserDto userDto = underTest.saveUser(userToSave);

        // Then
        assertAll(
                () -> verify(userRepository, times(1)).findByEmail(userToSave.email()),
                () -> verify(userRepository, times(1)).save(any(User.class)),
                () -> assertNotNull(userDto),
                () -> assertNotNull(userDto.id()),
                () -> assertEquals(userToSave.firstName(), userDto.firstName()),
                () -> assertEquals(userToSave.lastName(), userDto.lastName()),
                () -> assertEquals(userToSave.email(), userDto.email())
        );
    }

    @Test
    @DisplayName("It should throw UserExistsException when user try to save new user with existing email")
    void itShouldThrowUserExistsExceptionWhenUserTryToSaveNewUserWithExistingEmail() {
        // Given
        RegistrationUserDto userToSave = RegistrationUserDto.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.pl")
                .password("verySecretPassword")
                .build();

        long id = 1L;
        UserRole userRole = UserRole.builder()
                .id(id)
                .name(Role.ADMIN.name())
                .description(Role.ADMIN.getDescription())
                .build();

        User user = User.builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("john@doe.pl")
                .password("verySecretPassword")
                .userRole(userRole)
                .build();

        when(userRepository.findByEmail(userToSave.email())).thenReturn(Optional.of(user));

        // When && Then
        assertAll(
                () -> assertThrows(UserExistsException.class, () -> underTest.saveUser(userToSave)),
                () -> verify(userRepository, times(1)).findByEmail(userToSave.email())
        );
    }
}