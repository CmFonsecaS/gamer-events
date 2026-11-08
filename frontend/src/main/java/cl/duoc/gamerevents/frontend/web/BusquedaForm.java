package cl.duoc.gamerevents.frontend.web;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

/**
 * Filtros de la búsqueda pública. Se validan en el frontend y de nuevo en el backend.
 */
public class BusquedaForm {

    @Size(max = 80, message = "El nombre admite hasta 80 caracteres.")
    private String nombre;

    @Positive(message = "Videojuego no válido.")
    private Long videojuego;

    @Positive(message = "Categoría no válida.")
    private Long categoria;

    @Size(max = 60, message = "La ubicación admite hasta 60 caracteres.")
    private String ubicacion;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate desde;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate hasta;

    @Pattern(regexp = "PRESENCIAL|ONLINE", message = "Modalidad no válida.")
    private String modalidad;

    @AssertTrue(message = "La fecha 'desde' no puede ser posterior a la fecha 'hasta'.")
    public boolean isRangoFechasValido() {
        return desde == null || hasta == null || !desde.isAfter(hasta);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getVideojuego() {
        return videojuego;
    }

    public void setVideojuego(Long videojuego) {
        this.videojuego = videojuego;
    }

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public LocalDate getDesde() {
        return desde;
    }

    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    public LocalDate getHasta() {
        return hasta;
    }

    public void setHasta(LocalDate hasta) {
        this.hasta = hasta;
    }

    public String getModalidad() {
        return modalidad;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = (modalidad == null || modalidad.isBlank()) ? null : modalidad;
    }
}
