package cl.duoc.gamerevents.backend.evento;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.duoc.gamerevents.backend.evento.EventoDtos.BannerDto;
import cl.duoc.gamerevents.backend.evento.EventoDtos.EventoDetalle;
import cl.duoc.gamerevents.backend.evento.EventoDtos.EventoResumen;
import cl.duoc.gamerevents.backend.evento.EventoDtos.Filtros;
import cl.duoc.gamerevents.backend.evento.EventoDtos.Inicio;
import cl.duoc.gamerevents.backend.evento.EventoDtos.Opcion;

/**
 * Fachada (patrón Facade) del dominio de eventos: los controladores REST solo usan este servicio,
 * que coordina los repositorios y convierte las entidades en DTO (nunca se exponen entidades JPA).
 */
@Service
@Transactional(readOnly = true)
public class EventoService {

    private static final int DESTACADOS = 6;
    private static final int MAX_RESULTADOS = 50;

    private final EventoRepository eventos;
    private final VideojuegoRepository videojuegos;
    private final CategoriaRepository categorias;
    private final BannerRepository banners;

    public EventoService(EventoRepository eventos, VideojuegoRepository videojuegos,
            CategoriaRepository categorias, BannerRepository banners) {
        this.eventos = eventos;
        this.videojuegos = videojuegos;
        this.categorias = categorias;
        this.banners = banners;
    }

    public Inicio inicio() {
        PageRequest top = PageRequest.of(0, DESTACADOS);
        return new Inicio(
                eventos.findAllByOrderByFechaPublicacionDesc(top).stream().map(EventoResumen::de).toList(),
                eventos.findAllByOrderByInscritosDesc(top).stream().map(EventoResumen::de).toList(),
                banners.findByActivoTrueOrderByOrdenAsc().stream()
                        .map(b -> new BannerDto(b.getTitulo(), b.getTexto(), b.getImagen())).toList());
    }

    public List<EventoResumen> buscar(FiltroBusqueda filtro) {
        PageRequest pagina = PageRequest.of(0, MAX_RESULTADOS, Sort.by("fechaInicio").ascending());
        return eventos.findAll(EventoSpecs.desdeFiltro(filtro), pagina).stream().map(EventoResumen::de).toList();
    }

    public Optional<EventoDetalle> detalle(Long id) {
        return eventos.findWithDetalleById(id).map(EventoDetalle::de);
    }

    public Filtros filtros() {
        return new Filtros(
                videojuegos.findAllByOrderByNombreAsc().stream().map(v -> new Opcion(v.getId(), v.getNombre())).toList(),
                categorias.findAllByOrderByNombreAsc().stream().map(c -> new Opcion(c.getId(), c.getNombre())).toList(),
                eventos.findPaises(),
                eventos.findCiudades(),
                List.of(Modalidad.values()));
    }
}
