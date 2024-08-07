package pl.dk.aibron_first_task.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import pl.dk.aibron_first_task.exception.JwtAuthenticationException;
import pl.dk.aibron_first_task.user.UserRepository;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
class JwtService {

    private static final int EXP_TIME_SEC = 30 * 24 * 60 * 60;
    private final JWSAlgorithm jwsAlgorithm = JWSAlgorithm.HS256;
    private final JWSSigner jwsSigner;
    private final JWSVerifier jwsVerifier;
    private final UserRepository userRepository;

    public JwtService(@Value("${jws.sharedKey}") String sharedKey, UserRepository userRepository) {
        this.userRepository = userRepository;
        try {
            this.jwsSigner = new MACSigner(sharedKey.getBytes());
            this.jwsVerifier = new MACVerifier(sharedKey.getBytes());
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }

    public String createSignedJwt(String username, List<String> authorities) {
        JWSHeader header = new JWSHeader(jwsAlgorithm);
        LocalDateTime nowPlus1Month = LocalDateTime.now().plusSeconds(EXP_TIME_SEC);
        Date expirationDate = Date.from(nowPlus1Month.atZone(ZoneId.systemDefault()).toInstant());
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .expirationTime(expirationDate)
                .claim("authorities", authorities)
                .build();
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        try {
            signedJWT.sign(jwsSigner);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
        return signedJWT.serialize();
    }

    public void verifySignature(SignedJWT signedJWT) {
        try {
            boolean verify = signedJWT.verify(jwsVerifier);
            if (!verify) {
                throw new JwtAuthenticationException("JWT verification failed for token: [%s]".formatted(signedJWT.serialize()));
            }
        } catch (JOSEException e) {
            throw new JwtAuthenticationException("JWT verification failed for token: [%s]".formatted(signedJWT.serialize()));
        }
    }

    public void verifyExpirationTime(SignedJWT signedJWT) {
        try {
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            LocalDateTime expirationTime = jwtClaimsSet.getDateClaim("exp")
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            if (LocalDateTime.now().isAfter(expirationTime)) {
                throw new JwtAuthenticationException("Token expired at: [%s]".formatted(expirationTime));
            }
        } catch (ParseException e) {
            throw new JwtAuthenticationException("Token does not hava exp claims");
        }
    }

    public void verifyEmailFromToken(SignedJWT signedJWT) {
        try {
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            String userEmail = jwtClaimsSet.getSubject();
            if (userEmail != null) {
                userRepository.findByEmail(userEmail)
                        .orElseThrow(() -> new JwtAuthenticationException("Username [%s] from token not exists".formatted(userEmail)));
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Authentication createAuthentication(SignedJWT signedJWT) {
        String subject;
        List<String> authorities;
        try {
            JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();
            subject = jwtClaimsSet.getSubject();
            authorities = jwtClaimsSet.getStringListClaim("authorities");
        } catch (ParseException e) {
            throw new JwtAuthenticationException("Missing claims sub or authorities");
        }
        List<SimpleGrantedAuthority> grantedAuthorityList = authorities.stream().map(SimpleGrantedAuthority::new).toList();
        return new UsernamePasswordAuthenticationToken(subject, null, grantedAuthorityList);
    }
}
