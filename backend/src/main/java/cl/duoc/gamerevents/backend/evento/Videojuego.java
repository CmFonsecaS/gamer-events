package cl.duoc.gamerevents.backend.evento;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Videojuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String genero;

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getGenero() { return genero; }
}
