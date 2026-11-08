package cl.duoc.gamerevents.frontend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

import cl.duoc.gamerevents.frontend.auth.BackendAuthenticationProvider;

/**
 * Seguridad del sitio web:
 * - Públicas: inicio (/, /home y /eventos), búsqueda (/eventos/buscar), login y recursos estáticos.
 * - Privada: detalle de evento (/eventos/{id}), solo roles USER o ADMIN; sin sesión redirige al login.
 * - CSRF activo para todos los formularios POST (login y logout).
 * - Encabezados de seguridad (CSP, anti-clickjacking, referrer, permisos).
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String CSP = "default-src 'self'; script-src 'self'; style-src 'self'; "
            + "img-src 'self' data:; font-src 'self'; connect-src 'self'; object-src 'none'; "
            + "base-uri 'self'; form-action 'self'; frame-ancestors 'none'";

    @Bean
    SecurityFilterChain webSecurity(HttpSecurity http, BackendAuthenticationProvider backendAuth) throws Exception {
        http
            .authenticationProvider(backendAuth)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/", "/home", "/eventos", "/eventos/buscar", "/login").permitAll()
                .requestMatchers(HttpMethod.GET, "/css/**", "/img/**", "/favicon.ico").permitAll()
                .requestMatchers("/error").permitAll()
                // Autorización por rol: el rol viene del backend (dentro del login/JWT).
                .requestMatchers(HttpMethod.GET, "/eventos/*").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .failureHandler(fallaLogin())
                .defaultSuccessUrl("/eventos", false)
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/eventos?salir")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("GE_SESSION"))
            .sessionManagement(s -> s
                // Nuevo identificador de sesión al iniciar sesión (evita fijación de sesión).
                .sessionFixation(f -> f.changeSessionId())
                .maximumSessions(1))
            .headers(h -> h
                .contentSecurityPolicy(c -> c.policyDirectives(CSP))
                .frameOptions(f -> f.deny())
                .referrerPolicy(r -> r.policy(ReferrerPolicy.SAME_ORIGIN))
                .permissionsPolicyHeader(p -> p.policy("camera=(), microphone=(), geolocation=(), payment=()")));
        return http.build();
    }

    /** Permite que el control de sesiones simultáneas sepa cuándo una sesión termina. */
    @Bean
    HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /** Redirige al login indicando el motivo, sin revelar si el usuario existe. */
    private static AuthenticationFailureHandler fallaLogin() {
        return (request, response, ex) -> {
            String motivo = "error";
            if (ex instanceof LockedException) {
                motivo = "bloqueado";
            } else if (ex instanceof AuthenticationServiceException) {
                motivo = "servicio";
            }
            response.sendRedirect(request.getContextPath() + "/login?" + motivo);
        };
    }
}
