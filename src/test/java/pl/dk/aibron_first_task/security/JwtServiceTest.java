package pl.dk.aibron_first_task.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import pl.dk.aibron_first_task.exception.JwtAuthenticationException;
import pl.dk.aibron_first_task.user.UserRepository;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class JwtServiceTest {

    private JwtService underTest;
    private JWSSigner jwsSigner;
    private JWSVerifier jwsVerifier;
    private String sharedKeyForTests = "8e55772b-26c5-4114-bbe9-cb6d44af2ce4";
    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void init() throws JOSEException {
        MockitoAnnotations.openMocks(this);
        jwsSigner = new MACSigner(sharedKeyForTests.getBytes());
        jwsVerifier = new MACVerifier(sharedKeyForTests.getBytes());
        underTest = new JwtService(sharedKeyForTests, userRepository);
    }

    @Test
    @DisplayName("It should create JWT")
    void itShouldCreteJWT() throws ParseException {
        // Given
        String username = "john@john@test.pl";
        List<String> authorities = List.of("CUSTOMER");
        int expirationTime = 30 * 24 * 60 * 60;

        // When
        String jwt = underTest.createSignedJwt(username, authorities);

        // Then
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        List<String> authoritiesFromCreatedJWT = signedJWT.getJWTClaimsSet().getStringListClaim("authorities");
        Date expirationTimeFromCratedJWT = signedJWT.getJWTClaimsSet().getExpirationTime();
        String subjectFromCreatedJWT = signedJWT.getJWTClaimsSet().getSubject();

        assertAll(
                () -> assertThat(subjectFromCreatedJWT).isEqualTo(username),
                () -> assertThat(authoritiesFromCreatedJWT).isEqualTo(authorities),
                () -> assertThat(expirationTimeFromCratedJWT.toInstant()).isBeforeOrEqualTo(Instant.now().plusSeconds(expirationTime))
        );
    }

    @Test
    @DisplayName("It should verify signature")
    void itShouldVerifySignature() throws ParseException {
        // Given
        String username = "john@john@test.pl";
        List<String> authorities = List.of("CUSTOMER");
        String jwt = underTest.createSignedJwt(username, authorities);
        SignedJWT signedJWT = SignedJWT.parse(jwt);

        // When
        // Then
        assertDoesNotThrow(() -> underTest.verifySignature(signedJWT));
    }

    @Test
    @DisplayName("It should verify expiration time")
    void itShouldVerifyExpirationTime() throws ParseException {
        // Given
        String username = "john@john@test.pl";
        List<String> authorities = List.of("CUSTOMER");
        String jwt = underTest.createSignedJwt(username, authorities);
        SignedJWT signedJWT = SignedJWT.parse(jwt);

        // When
        // Then
        assertDoesNotThrow(() -> underTest.verifyExpirationTime(signedJWT));
    }

    @Test
    @DisplayName("It should create authentication")
    void itShouldCreateAuthentication() throws ParseException {
        // Given
        String username = "john@john@test.pl";
        List<String> authorities = List.of("CUSTOMER");
        String jwt = underTest.createSignedJwt(username, authorities);
        SignedJWT signedJWT = SignedJWT.parse(jwt);

        // When
        Authentication authentication = underTest.createAuthentication(signedJWT);

        Optional<? extends GrantedAuthority> grantedAuthority = authentication.getAuthorities().stream().findAny();
        // Then
        assertAll(
                () -> assertThat(authentication.getName()).isEqualTo(username),
                () -> assertThat(grantedAuthority).isPresent().hasValueSatisfying(
                        granted -> assertThat(granted.getAuthority()).isEqualTo(authorities.stream().findAny().get()))
        );
    }

    @Test
    @DisplayName("It should throw JWT authentication exception")
    void itShouldThrowJwtAuthenticationException() throws ParseException {
        // Given
        SignedJWT signedJWT = mock(SignedJWT.class);

        // When
        // Then
        assertThrows(JwtAuthenticationException.class, () -> underTest.verifySignature(signedJWT));
    }

}