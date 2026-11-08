package cl.duoc.gamerevents.backend.common;

/** Utilidades para registrar datos del usuario sin permitir inyección en los logs. */
public final class LogSeguro {

    private LogSeguro() {
    }

    /** Quita saltos de línea y caracteres de control, y limita el largo. */
    public static String limpiar(String valor) {
        if (valor == null) {
            return "";
        }
        String limpio = valor.replaceAll("[\\p{Cntrl}]", "_");
        return limpio.length() > 50 ? limpio.substring(0, 50) + "..." : limpio;
    }
}
