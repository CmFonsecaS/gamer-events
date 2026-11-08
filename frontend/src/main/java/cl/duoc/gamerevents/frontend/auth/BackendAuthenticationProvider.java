package cl.duoc.gamerevents.frontend.auth;

import java.time.Instant;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import cl.duoc.gamerevents.frontend.api.ApiModels.LoginResponse;
import cl.duoc.gamerevents.frontend.api.BackendClient;
import cl.duoc.gamerevents.frontend.api.BackendNoDisponibleException;

/**
 * Autentica el formulario de login contra la API del backend (POST /login).
 * Si el backend acepta las credenciales, guarda el JWT en el usuario de la sesión.
 * No registra contraseñas ni tokens en los logs.
 */
@Component
public class BackendAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(BackendAuthenticationProvider.class);
    private static final Set<String> ROLES_VALIDOS = Set.of("USER", "ADMIN");

    private final BackendClient backend;

    public BackendAuthenticationProvider(BackendClient backend) {
        this.backend = backend;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName() == null ? "" : authentication.getName().trim();
        String password = authentication.getCredentials() == null ? "" : authentication.getCredentials().toString();

        if (username.isEmpty() || password.isEmpty() || username.length() > 50 || password.length() > 100) {
            throw new BadCredentialsException("Credenciales no válidas");
        }

        LoginResponse respuesta;
        try {
            respuesta = backend.login(username, password);
        } catch (BackendNoDisponibleException e) {
            log.error("No se pudo contactar al backend para autenticar");
            throw new AuthenticationServiceException("Servicio no disponible", e);
        }

        // El rol proviene de la identidad emitida por el backend; no se asume ninguno por defecto.
        if (respuesta == null || respuesta.token() == null || !ROLES_VALIDOS.contains(respuesta.rol())) {
            throw new AuthenticationServiceException("Respuesta de autenticación no válida");
        }

        UsuarioSesion usuario = new UsuarioSesion(username, respuesta.nombre(), respuesta.rol(), respuesta.token(),
                Instant.now().plusSeconds(respuesta.expiraEnSegundos()));
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + respuesta.rol()));
        // La contraseña no se conserva en la sesión (credentials = null).
        return UsernamePasswordAuthenticationToken.authenticated(usuario, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
