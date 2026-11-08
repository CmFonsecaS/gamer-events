package cl.duoc.gamerevents.backend.evento;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

/**
 * Construye la consulta de búsqueda con Criteria API. Los valores del usuario siempre viajan
 * como parámetros enlazados (nunca concatenados en SQL), lo que previene inyección SQL.
 */
final class EventoSpecs {

    private EventoSpecs() {
    }

    static Specification<Evento> desdeFiltro(FiltroBusqueda f) {
        return (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();
            if (tieneTexto(f.nombre())) {
                p.add(cb.like(cb.lower(root.get("nombre")), patronLike(f.nombre()), '\\'));
            }
            if (f.videojuego() != null) {
                p.add(cb.equal(root.get("videojuego").get("id"), f.videojuego()));
            }
            if (f.categoria() != null) {
                p.add(cb.equal(root.get("categoria").get("id"), f.categoria()));
            }
            if (tieneTexto(f.ubicacion())) {
                String patron = patronLike(f.ubicacion());
                p.add(cb.or(
                        cb.like(cb.lower(root.get("pais")), patron, '\\'),
                        cb.like(cb.lower(root.get("ciudad")), patron, '\\')));
            }
            // Un evento coincide con el rango si ocurre (aunque sea en parte) entre "desde" y "hasta".
            if (f.desde() != null) {
                p.add(cb.greaterThanOrEqualTo(root.get("fechaFin"), f.desde().atStartOfDay()));
            }
            if (f.hasta() != null) {
                p.add(cb.lessThan(root.get("fechaInicio"), f.hasta().plusDays(1).atStartOfDay()));
            }
            if (f.modalidad() != null) {
                p.add(cb.equal(root.get("modalidad"), f.modalidad()));
            }
            return cb.and(p.toArray(Predicate[]::new));
        };
    }

    private static boolean tieneTexto(String s) {
        return s != null && !s.isBlank();
    }

    /** Escapa los comodines de LIKE para que se busquen como texto literal. */
    private static String patronLike(String texto) {
        String limpio = texto.trim().toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
        return "%" + limpio + "%";
    }
}
