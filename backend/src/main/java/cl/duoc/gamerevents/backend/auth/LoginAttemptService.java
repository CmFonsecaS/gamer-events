package cl.duoc.gamerevents.backend.auth;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

/**
 * Bloqueo temporal tras varios intentos fallidos de login (mitiga ataques de fuerza bruta).
 * Se guarda en memoria: suficiente para una instancia del backend.
 */
@Service
public class LoginAttemptService {

    static final int MAX_INTENTOS = 5;
    static final Duration BLOQUEO = Duration.ofMinutes(5);

    private record Estado(int fallos, Instant bloqueadoHasta) {
    }

    private final Map<String, Estado> estados = new ConcurrentHashMap<>();
    private final Clock clock;

    public LoginAttemptService() {
        this(Clock.systemUTC());
    }

    LoginAttemptService(Clock clock) {
        this.clock = clock;
    }

    public boolean estaBloqueado(String username) {
        Estado e = estados.get(clave(username));
        return e != null && e.bloqueadoHasta() != null && clock.instant().isBefore(e.bloqueadoHasta());
    }

    public void registrarFallo(String username) {
        estados.compute(clave(username), (k, e) -> {
            int fallos = (e == null || bloqueoVencido(e)) ? 1 : e.fallos() + 1;
            Instant hasta = fallos >= MAX_INTENTOS ? clock.instant().plus(BLOQUEO) : null;
            return new Estado(fallos, hasta);
        });
    }

    public void registrarExito(String username) {
        estados.remove(clave(username));
    }

    private boolean bloqueoVencido(Estado e) {
        return e.bloqueadoHasta() != null && !clock.instant().isBefore(e.bloqueadoHasta());
    }

    private static String clave(String username) {
        return username == null ? "" : username.trim().toLowerCase(Locale.ROOT);
    }
}
