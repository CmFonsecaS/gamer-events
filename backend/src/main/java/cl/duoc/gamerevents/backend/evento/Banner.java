package cl.duoc.gamerevents.backend.evento;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Banner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String texto;

    private String imagen;

    private boolean activo;

    private int orden;

    public Long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getTexto() { return texto; }
    public String getImagen() { return imagen; }
    public boolean isActivo() { return activo; }
    public int getOrden() { return orden; }
}
