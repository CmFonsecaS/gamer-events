package cl.duoc.gamerevents.frontend.auth;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

/**
 * Usuario autenticado en el frontend. Vive dentro de la sesión HTTP de cada usuario
 * (nunca en un componente compartido), por lo que el JWT no se mezcla entre sesiones.
 * El token nunca se envía al navegador.
 */
public record UsuarioSesion(String username, String nombre, String rol, String token, Instant expiraEn)
        implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public boolean tokenVencido() {
        return Instant.now().isAfter(expiraEn);
    }

    /** No mostrar el JWT si el objeto llega a imprimirse en un log. */
    @Override
    public String toString() {
        return "UsuarioSesion[username=" + username + ", rol=" + rol + ", token=****]";
    }
}
