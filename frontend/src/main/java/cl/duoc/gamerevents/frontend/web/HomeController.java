package cl.duoc.gamerevents.frontend.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cl.duoc.gamerevents.frontend.api.BackendClient;

/**
 * Página de inicio (pública): eventos recientes, populares y banners comerciales.
 * Responde en "/", "/home" y "/eventos".
 */
@Controller
public class HomeController {

    private final BackendClient backend;

    public HomeController(BackendClient backend) {
        this.backend = backend;
    }

    @GetMapping({"/", "/home", "/eventos"})
    public String inicio(Model model) {
        model.addAttribute("inicio", backend.inicio());
        return "index";
    }
}
