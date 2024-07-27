package pl.dk.aibron_first_task.security;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.dk.aibron_first_task.security.dtos.UserCredentialsDto;
import pl.dk.aibron_first_task.user.UserRepository;


import java.util.Optional;

@Service
@AllArgsConstructor
class UserCredentialService {

    private final UserRepository userRepository;

    public Optional<UserCredentialsDto> findCredentialsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserCredentialsDtoMapper::map);
    }
}
