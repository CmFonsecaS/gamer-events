package cl.duoc.gamerevents.backend.evento;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String resumen;

    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "videojuego_id")
    private Videojuego videojuego;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organizador_id")
    private Organizador organizador;

    @Enumerated(EnumType.STRING)
    private Modalidad modalidad;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDateTime fechaFin;

    private String pais;

    private String ciudad;

    private String direccion;

    @Column(name = "enlace_acceso")
    private String enlaceAcceso;

    private String requisitos;

    @Column(name = "info_inscripcion")
    private String infoInscripcion;

    private int precio;

    private int cupos;

    private int inscritos;

    @Column(name = "imagen_portada")
    private String imagenPortada;

    @Column(name = "fecha_publicacion")
    private LocalDateTime fechaPublicacion;

    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY)
    @OrderBy("orden ASC")
    private List<EventoFoto> fotos = new ArrayList<>();

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getResumen() { return resumen; }
    public String getDescripcion() { return descripcion; }
    public Videojuego getVideojuego() { return videojuego; }
    public Categoria getCategoria() { return categoria; }
    public Organizador getOrganizador() { return organizador; }
    public Modalidad getModalidad() { return modalidad; }
    public LocalDateTime getFechaInicio() { return fechaInicio; }
    public LocalDateTime getFechaFin() { return fechaFin; }
    public String getPais() { return pais; }
    public String getCiudad() { return ciudad; }
    public String getDireccion() { return direccion; }
    public String getEnlaceAcceso() { return enlaceAcceso; }
    public String getRequisitos() { return requisitos; }
    public String getInfoInscripcion() { return infoInscripcion; }
    public int getPrecio() { return precio; }
    public int getCupos() { return cupos; }
    public int getInscritos() { return inscritos; }
    public String getImagenPortada() { return imagenPortada; }
    public LocalDateTime getFechaPublicacion() { return fechaPublicacion; }
    public List<EventoFoto> getFotos() { return fotos; }
}
