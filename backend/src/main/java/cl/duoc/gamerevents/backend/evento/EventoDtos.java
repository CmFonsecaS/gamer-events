package cl.duoc.gamerevents.backend.evento;

import java.time.LocalDateTime;
import java.util.List;

/** Objetos que expone la API. Nunca se devuelven entidades JPA directamente. */
public final class EventoDtos {

    private EventoDtos() {
    }

    public record EventoResumen(Long id, String nombre, String resumen, String videojuego, String categoria,
            Modalidad modalidad, LocalDateTime fechaInicio, String pais, String ciudad, int precio, int cupos,
            int inscritos, String imagenPortada) {

        static EventoResumen de(Evento e) {
            return new EventoResumen(e.getId(), e.getNombre(), e.getResumen(), e.getVideojuego().getNombre(),
                    e.getCategoria().getNombre(), e.getModalidad(), e.getFechaInicio(), e.getPais(), e.getCiudad(),
                    e.getPrecio(), e.getCupos(), e.getInscritos(), e.getImagenPortada());
        }
    }

    public record Foto(String url, String descripcion) {
    }

    public record OrganizadorDto(String nombre, String email, String sitioWeb) {
    }

    public record EventoDetalle(Long id, String nombre, String resumen, String descripcion, String videojuego,
            String generoVideojuego, String categoria, Modalidad modalidad, LocalDateTime fechaInicio,
            LocalDateTime fechaFin, String pais, String ciudad, String direccion, String enlaceAcceso,
            OrganizadorDto organizador, String requisitos, String infoInscripcion, int precio, int cupos,
            int inscritos, String imagenPortada, List<Foto> fotos) {

        static EventoDetalle de(Evento e) {
            Organizador o = e.getOrganizador();
            return new EventoDetalle(e.getId(), e.getNombre(), e.getResumen(), e.getDescripcion(),
                    e.getVideojuego().getNombre(), e.getVideojuego().getGenero(), e.getCategoria().getNombre(),
                    e.getModalidad(), e.getFechaInicio(), e.getFechaFin(), e.getPais(), e.getCiudad(),
                    e.getDireccion(), e.getEnlaceAcceso(),
                    new OrganizadorDto(o.getNombre(), o.getEmail(), o.getSitioWeb()),
                    e.getRequisitos(), e.getInfoInscripcion(), e.getPrecio(), e.getCupos(), e.getInscritos(),
                    e.getImagenPortada(),
                    e.getFotos().stream().map(f -> new Foto(f.getUrl(), f.getDescripcion())).toList());
        }
    }

    public record BannerDto(String titulo, String texto, String imagen) {
    }

    public record Inicio(List<EventoResumen> recientes, List<EventoResumen> populares, List<BannerDto> banners) {
    }

    public record Opcion(Long id, String nombre) {
    }

    public record Filtros(List<Opcion> videojuegos, List<Opcion> categorias, List<String> paises,
            List<String> ciudades, List<Modalidad> modalidades) {
    }
}
