package cl.duoc.gamerevents.frontend;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import cl.duoc.gamerevents.frontend.api.ApiModels.Banner;
import cl.duoc.gamerevents.frontend.api.ApiModels.EventoDetalle;
import cl.duoc.gamerevents.frontend.api.ApiModels.EventoResumen;
import cl.duoc.gamerevents.frontend.api.ApiModels.Filtros;
import cl.duoc.gamerevents.frontend.api.ApiModels.Foto;
import cl.duoc.gamerevents.frontend.api.ApiModels.Inicio;
import cl.duoc.gamerevents.frontend.api.ApiModels.LoginResponse;
import cl.duoc.gamerevents.frontend.api.ApiModels.Opcion;
import cl.duoc.gamerevents.frontend.api.ApiModels.Organizador;
import cl.duoc.gamerevents.frontend.api.BackendClient;
import cl.duoc.gamerevents.frontend.api.SesionExpiradaException;
import cl.duoc.gamerevents.frontend.auth.UsuarioSesion;

/**
 * Pruebas de la capa web con el backend simulado: rutas públicas y privadas, login, CSRF,
 * encabezados de seguridad, escape de HTML y validación de filtros.
 */
@SpringBootTest
@AutoConfigureMockMvc
class WebSeguridadTests {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    BackendClient backend;

    private static final LocalDateTime FECHA = LocalDateTime.of(2026, 12, 18, 10, 0);

    private static EventoResumen resumen(String nombre) {
        return new EventoResumen(1L, nombre, "Resumen", "Valorant", "Torneo", "PRESENCIAL", FECHA, "Chile",
                "Santiago", 5000, 100, 40, "/img/eventos/copa-andes-valorant.svg");
    }

    private static EventoDetalle detalle(String enlace) {
        return new EventoDetalle(1L, "Copa Andes", "Resumen", "Descripción", "Valorant", "FPS", "Torneo",
                "ONLINE", FECHA, FECHA.plusHours(8), "Chile", "Santiago", null, enlace,
                new Organizador("Org", "org@example.com", "https://example.com"), "Requisitos", "Inscripción",
                0, 100, 40, "/img/eventos/copa-andes-valorant.svg",
                List.of(new Foto("/img/eventos/copa-andes-valorant-1.svg", "Foto 1")));
    }

    private static UsernamePasswordAuthenticationToken usuario(Instant expira) {
        UsuarioSesion u = new UsuarioSesion("jugador1", "Jugador Uno", "USER", "token-de-prueba", expira);
        return UsernamePasswordAuthenticationToken.authenticated(u, null,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @BeforeEach
    void datos() {
        when(backend.inicio()).thenReturn(new Inicio(List.of(resumen("Evento Reciente")),
                List.of(resumen("Evento Popular")), List.of(new Banner("Tienda", "Oferta", "/img/banners/novanet.svg"))));
        when(backend.filtros()).thenReturn(new Filtros(List.of(new Opcion(1L, "Valorant")),
                List.of(new Opcion(1L, "Torneo")), List.of("Chile"), List.of("Santiago"),
                List.of("PRESENCIAL", "ONLINE")));
        when(backend.buscar(any())).thenReturn(new EventoResumen[] {resumen("Copa Andes Valorant")});
    }

    // ---------- Rutas públicas ----------

    @Test
    void inicioEsPublico() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Evento Reciente")))
                .andExpect(content().string(containsString("Evento Popular")))
                .andExpect(content().string(containsString("Tienda")))
                .andExpect(content().string(containsString("Iniciar sesión")));
    }

    @Test
    void rutaEventosCargaLaPaginaDeInicio() throws Exception {
        mvc.perform(get("/eventos"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Eventos más recientes")));
        mvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Eventos más recientes")));
    }

    @Test
    void detalleConRolNoAutorizadoEsRechazado() throws Exception {
        UsuarioSesion u = new UsuarioSesion("x", "X", "INVITADO", "t", Instant.now().plusSeconds(600));
        var auth = UsernamePasswordAuthenticationToken.authenticated(u, null,
                List.of(new SimpleGrantedAuthority("ROLE_INVITADO")));
        mvc.perform(get("/eventos/1").with(authentication(auth)))
                .andExpect(status().isForbidden());
        verify(backend, never()).detalle(anyLong(), anyString());
    }

    @Test
    void busquedaEsPublicaYEnviaLosFiltros() throws Exception {
        mvc.perform(get("/eventos/buscar").param("nombre", "copa").param("videojuego", "1").param("categoria", "1")
                        .param("ubicacion", "Chile").param("modalidad", "PRESENCIAL")
                        .param("desde", "2026-12-01").param("hasta", "2026-12-31"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Copa Andes Valorant")))
                .andExpect(content().string(containsString("1 evento encontrado")));
        verify(backend).buscar(any());
    }

    // ---------- Ruta privada ----------

    @Test
    void detalleSinSesionRedirigeAlLogin() throws Exception {
        mvc.perform(get("/eventos/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
        verify(backend, never()).detalle(anyLong(), anyString());
    }

    @Test
    void detalleConSesionMuestraDatosYUsaElTokenDeLaSesion() throws Exception {
        when(backend.detalle(1L, "token-de-prueba")).thenReturn(detalle("https://meet.example.com/sala"));
        mvc.perform(get("/eventos/1").with(authentication(usuario(Instant.now().plusSeconds(600)))))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Copa Andes")))
                .andExpect(content().string(containsString("https://meet.example.com/sala")))
                .andExpect(content().string(containsString("Foto 1")))
                // El JWT nunca llega al navegador.
                .andExpect(content().string(not(containsString("token-de-prueba"))));
        verify(backend).detalle(eq(1L), eq("token-de-prueba"));
    }

    @Test
    void enlaceConJavascriptNoSePublica() throws Exception {
        when(backend.detalle(1L, "token-de-prueba")).thenReturn(detalle("javascript:alert(1)"));
        mvc.perform(get("/eventos/1").with(authentication(usuario(Instant.now().plusSeconds(600)))))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("javascript:"))));
    }

    @Test
    void tokenVencidoObligaAIniciarSesionDeNuevo() throws Exception {
        mvc.perform(get("/eventos/1").with(authentication(usuario(Instant.now().minusSeconds(1)))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?expirada"));
    }

    @Test
    void tokenRechazadoPorBackendObligaAIniciarSesionDeNuevo() throws Exception {
        when(backend.detalle(anyLong(), anyString())).thenThrow(new SesionExpiradaException());
        mvc.perform(get("/eventos/1").with(authentication(usuario(Instant.now().plusSeconds(600)))))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?expirada"));
    }

    @Test
    void idNoNumericoDevuelve400SinDetallesTecnicos() throws Exception {
        mvc.perform(get("/eventos/abc").with(authentication(usuario(Instant.now().plusSeconds(600)))))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(not(containsString("Exception"))));
    }

    // ---------- Login / logout / CSRF ----------

    @Test
    void loginCorrectoCreaSesion() throws Exception {
        when(backend.login("jugador1", "Gamer#2026"))
                .thenReturn(new LoginResponse("jwt", "Bearer", 1800, "Jugador Uno", "USER"));
        mvc.perform(post("/login").param("username", "jugador1").param("password", "Gamer#2026").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/eventos"));
    }

    @Test
    void loginIncorrectoVuelveAlLoginConMensajeGenerico() throws Exception {
        when(backend.login(anyString(), anyString())).thenThrow(new BadCredentialsException("x"));
        mvc.perform(post("/login").param("username", "jugador1").param("password", "mala").with(csrf()))
                .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    void loginSinTokenCsrfEsRechazado() throws Exception {
        mvc.perform(post("/login").param("username", "jugador1").param("password", "Gamer#2026"))
                .andExpect(status().isForbidden());
        verify(backend, never()).login(anyString(), anyString());
    }

    @Test
    void logoutSinTokenCsrfEsRechazado() throws Exception {
        mvc.perform(post("/logout").with(authentication(usuario(Instant.now().plusSeconds(600)))))
                .andExpect(status().isForbidden());
    }

    @Test
    void logoutConCsrfCierraLaSesion() throws Exception {
        mvc.perform(post("/logout").with(csrf()).with(authentication(usuario(Instant.now().plusSeconds(600)))))
                .andExpect(redirectedUrl("/eventos?salir"));
    }

    @Test
    void formularioDeLoginIncluyeTokenCsrf() throws Exception {
        mvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("name=\"_csrf\"")));
    }

    // ---------- Entradas maliciosas y encabezados ----------

    @Test
    void textoMaliciosoSeMuestraEscapado() throws Exception {
        mvc.perform(get("/eventos/buscar").param("nombre", "<script>alert(1)</script>"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("<script>alert(1)</script>"))))
                .andExpect(content().string(containsString("&lt;script&gt;")));
    }

    @Test
    void filtrosNoValidosNoConsultanAlBackend() throws Exception {
        mvc.perform(get("/eventos/buscar").param("nombre", "x".repeat(81)).param("videojuego", "abc")
                        .param("modalidad", "HACK").param("desde", "2026-12-31").param("hasta", "2026-01-01"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Revisa los filtros")))
                .andExpect(content().string(not(containsString("NumberFormatException"))));
        verify(backend, never()).buscar(any());
    }

    @Test
    void respuestasIncluyenEncabezadosDeSeguridad() throws Exception {
        mvc.perform(get("/"))
                .andExpect(header().string("Content-Security-Policy", containsString("default-src 'self'")))
                .andExpect(header().string("Content-Security-Policy", containsString("frame-ancestors 'none'")))
                .andExpect(header().string("X-Frame-Options", "DENY"))
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().exists("Referrer-Policy"))
                .andExpect(header().exists("Permissions-Policy"));
    }
}
