package cl.duoc.gamerevents.frontend.api;

import java.time.LocalDateTime;
import java.util.List;

/** Datos que entrega la API del backend (mismos nombres de campos que sus DTO). */
public final class ApiModels {

    private ApiModels() {
    }

    public record LoginRequest(String username, String password) {
        @Override
        public String toString() {
            return "LoginRequest[username=" + username + ", password=****]";
        }
    }

    public record LoginResponse(String token, String tipo, long expiraEnSegundos, String nombre, String rol) {
        @Override
        public String toString() {
            return "LoginResponse[nombre=" + nombre + ", rol=" + rol + ", token=****]";
        }
    }

    public record EventoResumen(Long id, String nombre, String resumen, String videojuego, String categoria,
            String modalidad, LocalDateTime fechaInicio, String pais, String ciudad, int precio, int cupos,
            int inscritos, String imagenPortada) {
    }

    public record Foto(String url, String descripcion) {
    }

    public record Organizador(String nombre, String email, String sitioWeb) {
    }

    public record EventoDetalle(Long id, String nombre, String resumen, String descripcion, String videojuego,
            String generoVideojuego, String categoria, String modalidad, LocalDateTime fechaInicio,
            LocalDateTime fechaFin, String pais, String ciudad, String direccion, String enlaceAcceso,
            Organizador organizador, String requisitos, String infoInscripcion, int precio, int cupos,
            int inscritos, String imagenPortada, List<Foto> fotos) {
    }

    public record Banner(String titulo, String texto, String imagen) {
    }

    public record Inicio(List<EventoResumen> recientes, List<EventoResumen> populares, List<Banner> banners) {
    }

    public record Opcion(Long id, String nombre) {
    }

    public record Filtros(List<Opcion> videojuegos, List<Opcion> categorias, List<String> paises,
            List<String> ciudades, List<String> modalidades) {
    }
}
