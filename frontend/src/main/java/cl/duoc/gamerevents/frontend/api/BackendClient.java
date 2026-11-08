package cl.duoc.gamerevents.frontend.api;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

import cl.duoc.gamerevents.frontend.api.ApiModels.EventoDetalle;
import cl.duoc.gamerevents.frontend.api.ApiModels.EventoResumen;
import cl.duoc.gamerevents.frontend.api.ApiModels.Filtros;
import cl.duoc.gamerevents.frontend.api.ApiModels.Inicio;
import cl.duoc.gamerevents.frontend.api.ApiModels.LoginRequest;
import cl.duoc.gamerevents.frontend.api.ApiModels.LoginResponse;
import cl.duoc.gamerevents.frontend.web.BusquedaForm;

/**
 * Fachada (patrón Facade) de la comunicación con el backend: los controladores web solo usan esta
 * clase y no conocen URLs, encabezados ni formatos de la API. Todos los datos de eventos vienen de aquí
 * (MySQL vía backend).
 * Los parámetros se envían como variables de plantilla URI, que se codifican siempre
 * (un valor como "{x}" o "&a=b" no puede alterar la consulta).
 */
@Component
public class BackendClient {

    private final RestClient rest;

    public BackendClient(@Value("${app.backend.url}") String baseUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(10));
        this.rest = RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
    }

    public LoginResponse login(String username, String password) {
        try {
            return rest.post().uri("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new LoginRequest(username, password))
                    .retrieve()
                    .body(LoginResponse.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
                throw new LockedException("Usuario bloqueado temporalmente");
            }
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new BadCredentialsException("Credenciales no válidas");
            }
            throw new BackendNoDisponibleException("Error del backend en login", e);
        } catch (RestClientException e) {
            throw new BackendNoDisponibleException("Backend no disponible", e);
        }
    }

    public Inicio inicio() {
        return llamar(() -> rest.get().uri("/api/publico/inicio").retrieve().body(Inicio.class));
    }

    public Filtros filtros() {
        return llamar(() -> rest.get().uri("/api/publico/filtros").retrieve().body(Filtros.class));
    }

    public EventoResumen[] buscar(BusquedaForm f) {
        Map<String, Object> params = new LinkedHashMap<>();
        agregar(params, "nombre", f.getNombre());
        agregar(params, "videojuego", f.getVideojuego());
        agregar(params, "categoria", f.getCategoria());
        agregar(params, "ubicacion", f.getUbicacion());
        agregar(params, "desde", f.getDesde());
        agregar(params, "hasta", f.getHasta());
        agregar(params, "modalidad", f.getModalidad());

        return llamar(() -> rest.get()
                .uri(b -> {
                    b.path("/api/publico/eventos");
                    params.keySet().forEach(k -> b.queryParam(k, "{" + k + "}"));
                    return b.build(params);
                })
                .retrieve()
                .body(EventoResumen[].class));
    }

    /** Detalle privado: se envía el JWT de la sesión del usuario. */
    public EventoDetalle detalle(long id, String token) {
        try {
            return rest.get().uri("/api/eventos/{id}", id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(EventoDetalle.class);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new SesionExpiradaException();
            }
            if (e.getStatusCode() == HttpStatus.NOT_FOUND || e.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
            throw new BackendNoDisponibleException("Error del backend en detalle", e);
        } catch (RestClientException e) {
            throw new BackendNoDisponibleException("Backend no disponible", e);
        }
    }

    private static void agregar(Map<String, Object> params, String nombre, Object valor) {
        if (valor != null && !valor.toString().isBlank()) {
            params.put(nombre, valor.toString().trim());
        }
    }

    private static <T> T llamar(java.util.function.Supplier<T> llamada) {
        try {
            return llamada.get();
        } catch (RestClientException e) {
            throw new BackendNoDisponibleException("Backend no disponible", e);
        }
    }
}
