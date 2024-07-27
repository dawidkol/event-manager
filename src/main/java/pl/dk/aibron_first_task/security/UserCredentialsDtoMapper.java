package pl.dk.aibron_first_task.security;


import pl.dk.aibron_first_task.security.dtos.UserCredentialsDto;
import pl.dk.aibron_first_task.user.User;

class UserCredentialsDtoMapper {

    public static UserCredentialsDto map(User user) {
        return UserCredentialsDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getUserRole().getName())
                .build();
    }
}
