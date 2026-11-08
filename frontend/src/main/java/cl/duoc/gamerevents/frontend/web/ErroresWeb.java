package cl.duoc.gamerevents.frontend.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import cl.duoc.gamerevents.frontend.api.BackendNoDisponibleException;
import cl.duoc.gamerevents.frontend.api.SesionExpiradaException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/** Errores de la web: mensajes amigables, sin detalles técnicos para el usuario. */
@ControllerAdvice
public class ErroresWeb {

    private static final Logger log = LoggerFactory.getLogger(ErroresWeb.class);

    /** JWT vencido o rechazado: se cierra la sesión local y se pide iniciar sesión otra vez. */
    @ExceptionHandler(SesionExpiradaException.class)
    public String sesionExpirada(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return "redirect:/login?expirada";
    }

    @ExceptionHandler(BackendNoDisponibleException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ModelAndView backendCaido(BackendNoDisponibleException ex) {
        log.error("Backend no disponible: {}", ex.getMessage());
        return vistaError(HttpStatus.SERVICE_UNAVAILABLE,
                "El servicio de eventos no está disponible en este momento. Intenta nuevamente en unos minutos.");
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HandlerMethodValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView solicitudInvalida() {
        return vistaError(HttpStatus.BAD_REQUEST, "La dirección solicitada no es válida.");
    }

    private static ModelAndView vistaError(HttpStatus status, String mensaje) {
        ModelAndView mv = new ModelAndView("error");
        mv.addObject("status", status.value());
        mv.addObject("mensaje", mensaje);
        mv.setStatus(status);
        return mv;
    }
}
