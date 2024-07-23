package pl.dk.aibron_first_task.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @NotBlank
        @Size(min = 3, max = 50)
        private String firstName;
        @NotBlank
        @Size(min = 3, max = 50)
        private String lastName;
        @Email
        @Column(unique = true)
        private String email;
        @NotBlank
        @Size(min = 6, max = 200)
        private String password;
        @ManyToOne
        @JoinColumn(name = "role_id")
        private UserRole userRole;
}


