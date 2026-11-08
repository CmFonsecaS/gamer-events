package cl.duoc.gamerevents.backend.evento;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "evento_foto")
public class EventoFoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    private String url;

    private String descripcion;

    private int orden;

    public Long getId() { return id; }
    public String getUrl() { return url; }
    public String getDescripcion() { return descripcion; }
    public int getOrden() { return orden; }
}
