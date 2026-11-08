package cl.duoc.gamerevents.backend.evento;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.gamerevents.backend.evento.EventoDtos.EventoDetalle;
import cl.duoc.gamerevents.backend.evento.EventoDtos.EventoResumen;
import cl.duoc.gamerevents.backend.evento.EventoDtos.Filtros;
import cl.duoc.gamerevents.backend.evento.EventoDtos.Inicio;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

/**
 * API de eventos. Las rutas /api/publico/** son públicas; /api/eventos/** exige un JWT válido
 * (ver SecurityConfig).
 */
@RestController
@Validated
public class EventoController {

    private final EventoService service;

    public EventoController(EventoService service) {
        this.service = service;
    }

    @GetMapping("/api/publico/inicio")
    public Inicio inicio() {
        return service.inicio();
    }

    @GetMapping("/api/publico/filtros")
    public Filtros filtros() {
        return service.filtros();
    }

    @GetMapping("/api/publico/eventos")
    public List<EventoResumen> buscar(@Valid FiltroBusqueda filtro) {
        return service.buscar(filtro);
    }

    @GetMapping("/api/eventos/{id}")
    public ResponseEntity<EventoDetalle> detalle(@PathVariable @Positive Long id) {
        return ResponseEntity.of(service.detalle(id));
    }
}
