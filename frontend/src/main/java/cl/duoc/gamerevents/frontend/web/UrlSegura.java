package cl.duoc.gamerevents.frontend.web;

import java.net.URI;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

/**
 * Filtra las URL que vienen de la base de datos antes de ponerlas en la página
 * (uso en plantillas: ${@urlSegura.imagen(...)}). Evita enlaces "javascript:" u otros esquemas
 * peligrosos aunque alguien llegara a modificar los datos (defensa en profundidad contra XSS).
 */
@Component("urlSegura")
public class UrlSegura {

    private static final Pattern IMAGEN_LOCAL = Pattern.compile("^/img/[a-z0-9/_-]+\\.(svg|png|jpe?g|webp)$");
    private static final String IMAGEN_POR_DEFECTO = "/img/sin-imagen.svg";

    /** Solo se aceptan imágenes servidas por este mismo sitio. */
    public String imagen(String ruta) {
        return ruta != null && IMAGEN_LOCAL.matcher(ruta).matches() && !ruta.contains("..") ? ruta : IMAGEN_POR_DEFECTO;
    }

    /** Solo enlaces http/https bien formados; cualquier otro valor se descarta. */
    public String externa(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        try {
            URI uri = URI.create(url.trim());
            String esquema = uri.getScheme();
            if (("https".equalsIgnoreCase(esquema) || "http".equalsIgnoreCase(esquema)) && uri.getHost() != null) {
                return uri.toString();
            }
        } catch (IllegalArgumentException e) {
            // URL mal formada: se descarta
        }
        return null;
    }
}
