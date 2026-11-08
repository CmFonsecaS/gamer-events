package cl.duoc.gamerevents.frontend.api;

/** El backend rechazó el JWT de la sesión (vencido o no válido): hay que volver a iniciar sesión. */
public class SesionExpiradaException extends RuntimeException {

    public SesionExpiradaException() {
        super("La sesión expiró");
    }
}
