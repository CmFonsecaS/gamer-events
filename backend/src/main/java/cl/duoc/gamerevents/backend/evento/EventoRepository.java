package cl.duoc.gamerevents.backend.evento;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface EventoRepository extends JpaRepository<Evento, Long>, JpaSpecificationExecutor<Evento> {

    @EntityGraph(attributePaths = {"videojuego", "categoria"})
    List<Evento> findAllByOrderByFechaPublicacionDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"videojuego", "categoria"})
    List<Evento> findAllByOrderByInscritosDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"videojuego", "categoria", "organizador", "fotos"})
    Optional<Evento> findWithDetalleById(Long id);

    @Query("select distinct e.pais from Evento e order by e.pais")
    List<String> findPaises();

    @Query("select distinct e.ciudad from Evento e order by e.ciudad")
    List<String> findCiudades();
}
