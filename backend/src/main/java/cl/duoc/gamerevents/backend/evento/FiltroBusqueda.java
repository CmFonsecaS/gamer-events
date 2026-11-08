package cl.duoc.gamerevents.backend.evento;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Parámetros de búsqueda de eventos. Todos son opcionales y se validan antes de consultar.
 */
public record FiltroBusqueda(
        @Size(max = 80) String nombre,
        @Positive Long videojuego,
        @Positive Long categoria,
        @Size(max = 60) String ubicacion,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
        Modalidad modalidad) {
}
