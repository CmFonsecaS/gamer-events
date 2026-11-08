package cl.duoc.gamerevents.backend.auth;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

/** Emite los JWT firmados (HS256) que el frontend usa para llamar a las API privadas. */
@Service
public class JwtService {

    /** Permiso que exigen las API privadas de eventos. */
    public static final String SCOPE_EVENTOS = "eventos.leer";

    private final JwtEncoder encoder;
    private final String issuer;
    private final Duration duracion;

    public JwtService(JwtEncoder encoder,
            @Value("${app.jwt.issuer}") String issuer,
            @Value("${app.jwt.expiration-minutes}") long minutos) {
        this.encoder = encoder;
        this.issuer = issuer;
        this.duracion = Duration.ofMinutes(minutos);
    }

    public String emitir(Usuario usuario) {
        Instant ahora = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(usuario.getUsername())
                .id(UUID.randomUUID().toString())
                .issuedAt(ahora)
                .expiresAt(ahora.plus(duracion))
                .claim("scope", SCOPE_EVENTOS)
                .claim("roles", List.of(usuario.getRol()))
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    public long duracionSegundos() {
        return duracion.toSeconds();
    }
}
