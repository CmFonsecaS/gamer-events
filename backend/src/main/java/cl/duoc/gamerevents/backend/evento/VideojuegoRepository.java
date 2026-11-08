package cl.duoc.gamerevents.backend.evento;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {
    List<Videojuego> findAllByOrderByNombreAsc();
}
