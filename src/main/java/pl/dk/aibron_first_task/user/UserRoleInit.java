package pl.dk.aibron_first_task.user;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@AllArgsConstructor
final class UserRoleInit {

    private final UserRoleRepository userRoleRepository;

    @PostConstruct
    private void initRoles() {
        if (userRoleRepository.count() == 0) {
            Arrays.stream(Role.values())
                    .map(role -> UserRole
                            .builder()
                            .name(role.name())
                            .description(role.getDescription())
                            .build())
                    .forEach(userRoleRepository::save);
        }
    }
}

