package cl.duoc.gamerevents.frontend.api;

/** El backend no respondió o respondió con un error inesperado. */
public class BackendNoDisponibleException extends RuntimeException {

    public BackendNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
