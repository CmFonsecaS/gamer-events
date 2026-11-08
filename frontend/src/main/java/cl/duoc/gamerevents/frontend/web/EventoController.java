package cl.duoc.gamerevents.frontend.web;

import java.util.Arrays;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import cl.duoc.gamerevents.frontend.api.ApiModels.EventoResumen;
import cl.duoc.gamerevents.frontend.api.BackendClient;
import cl.duoc.gamerevents.frontend.api.SesionExpiradaException;
import cl.duoc.gamerevents.frontend.auth.UsuarioSesion;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

/**
 * Búsqueda de eventos con filtros (pública) y detalle del evento (privado, ver SecurityConfig).
 */
@Controller
public class EventoController {

    private final BackendClient backend;

    public EventoController(BackendClient backend) {
        this.backend = backend;
    }

    @GetMapping("/eventos/buscar")
    public String buscar(@Valid @ModelAttribute("busqueda") BusquedaForm busqueda, BindingResult errores,
            Model model) {
        model.addAttribute("filtros", backend.filtros());
        model.addAttribute("invalido", errores.hasErrors());
        if (errores.hasErrors()) {
            // Con filtros no válidos no se consulta al backend.
            model.addAttribute("resultados", List.of());
            return "eventos";
        }
        List<EventoResumen> resultados = Arrays.asList(backend.buscar(busqueda));
        model.addAttribute("resultados", resultados);
        return "eventos";
    }

    @GetMapping("/eventos/{id}")
    public String detalle(@PathVariable @Positive long id, @AuthenticationPrincipal UsuarioSesion usuario,
            Model model) {
        if (usuario == null || usuario.tokenVencido()) {
            throw new SesionExpiradaException();
        }
        model.addAttribute("evento", backend.detalle(id, usuario.token()));
        return "detalle";
    }
}
