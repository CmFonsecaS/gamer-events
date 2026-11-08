package cl.duoc.gamerevents.backend.auth;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.gamerevents.backend.auth.AuthDtos.LoginRequest;
import cl.duoc.gamerevents.backend.auth.AuthDtos.LoginResponse;
import cl.duoc.gamerevents.backend.common.LogSeguro;
import jakarta.validation.Valid;

/**
 * API de login: valida usuario y contraseña (BCrypt) contra MySQL y devuelve un JWT.
 * Acepta dos formatos en POST /login:
 * - JSON {"username","password"}: responde JSON con el token, nombre y rol (lo usa el frontend).
 * - Formulario user / encryptedPass: responde el token como texto.
 * Ambos comparten las mismas validaciones, el bloqueo por intentos y los registros.
 */
@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    /** Hash ficticio para que un usuario inexistente tarde lo mismo que uno existente. */
    private static final String HASH_FICTICIO = "$2a$12$Yq/T10c74EtHkU./UsWy9eXJ.Bfwnqvm2ZCIez.6zax.clpTH7XJ2";

    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final LoginAttemptService intentos;

    public AuthController(UsuarioRepository usuarios, PasswordEncoder passwordEncoder, JwtService jwtService,
            LoginAttemptService intentos) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.intentos = intentos;
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        Resultado r = autenticar(request.username(), request.password());
        if (r.error() != null) {
            return r.error();
        }
        Usuario u = r.usuario();
        return ResponseEntity.ok(new LoginResponse(jwtService.emitir(u), "Bearer",
                jwtService.duracionSegundos(), u.getNombre(), u.getRol()));
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<?> loginFormulario(@RequestParam("user") String user,
            @RequestParam("encryptedPass") String encryptedPass) {
        if (user.isBlank() || encryptedPass.isBlank() || user.length() > 50 || encryptedPass.length() > 100) {
            return problema(HttpStatus.BAD_REQUEST, "Parámetros de la solicitud no válidos.");
        }
        Resultado r = autenticar(user, encryptedPass);
        if (r.error() != null) {
            return r.error();
        }
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(jwtService.emitir(r.usuario()));
    }

    private Resultado autenticar(String usernameIngresado, String password) {
        String username = usernameIngresado.trim();
        String userLog = LogSeguro.limpiar(username);

        if (intentos.estaBloqueado(username)) {
            log.warn("Login rechazado: usuario bloqueado temporalmente usuario={}", userLog);
            return Resultado.fallo(problema(HttpStatus.TOO_MANY_REQUESTS,
                    "Demasiados intentos fallidos. Intente nuevamente en unos minutos."));
        }

        Optional<Usuario> usuario = usuarios.findByUsername(username);
        String hash = usuario.map(Usuario::getPasswordHash).orElse(HASH_FICTICIO);
        boolean claveOk = passwordEncoder.matches(password, hash);

        if (usuario.isEmpty() || !claveOk || !usuario.get().isActivo()) {
            intentos.registrarFallo(username);
            log.warn("Login fallido usuario={}", userLog);
            // Mensaje genérico: no revela si el usuario existe.
            return Resultado.fallo(problema(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos."));
        }

        intentos.registrarExito(username);
        log.info("Login exitoso usuario={}", userLog);
        return new Resultado(usuario.get(), null);
    }

    private static ResponseEntity<ProblemDetail> problema(HttpStatus status, String detalle) {
        return ResponseEntity.status(status).body(ProblemDetail.forStatusAndDetail(status, detalle));
    }

    private record Resultado(Usuario usuario, ResponseEntity<ProblemDetail> error) {
        static Resultado fallo(ResponseEntity<ProblemDetail> error) {
            return new Resultado(null, error);
        }
    }
}
