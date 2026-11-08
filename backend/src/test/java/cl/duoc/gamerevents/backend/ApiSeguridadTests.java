package cl.duoc.gamerevents.backend;

import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

/**
 * Pruebas de integración contra la base MySQL local (Docker debe estar levantado).
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApiSeguridadTests {

    @Autowired
    MockMvc mvc;

    private String login(String user, String pass) throws Exception {
        String body = mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + user + "\",\"password\":\"" + pass + "\"}"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(body, "$.token");
    }

    // ---------- Rutas públicas ----------

    @Test
    void inicioEsPublicoYTraeRecientesPopularesYBanners() throws Exception {
        mvc.perform(get("/api/publico/inicio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recientes", hasSize(6)))
                .andExpect(jsonPath("$.populares[0].nombre", is("Pixel Fest Santiago 2026")))
                .andExpect(jsonPath("$.banners", hasSize(3)));
    }

    @Test
    void busquedaPublicaPorCadaFiltro() throws Exception {
        mvc.perform(get("/api/publico/eventos").param("nombre", "valorant"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        mvc.perform(get("/api/publico/eventos").param("modalidad", "ONLINE"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[*].modalidad", everyItem(is("ONLINE"))));
        mvc.perform(get("/api/publico/eventos").param("ubicacion", "chile"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[*].pais", everyItem(is("Chile"))));
        mvc.perform(get("/api/publico/eventos").param("ubicacion", "Valparaíso"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
        mvc.perform(get("/api/publico/eventos").param("categoria", "1").param("videojuego", "1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$[0].nombre", is("Copa Andes Valorant 2026")));
        mvc.perform(get("/api/publico/eventos").param("desde", "2027-01-01").param("hasta", "2027-01-31"))
                .andExpect(status().isOk()).andExpect(jsonPath("$.length()", greaterThan(2)));
    }

    @Test
    void busquedaNoPermiteInyeccionSqlNiComodines() throws Exception {
        mvc.perform(get("/api/publico/eventos").param("nombre", "' OR '1'='1"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
        mvc.perform(get("/api/publico/eventos").param("nombre", "%"))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void parametrosInvalidosResponden400SinDetallesInternos() throws Exception {
        mvc.perform(get("/api/publico/eventos").param("videojuego", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(not(containsString("Exception"))));
        mvc.perform(get("/api/publico/eventos").param("nombre", "x".repeat(200)))
                .andExpect(status().isBadRequest());
        mvc.perform(get("/api/publico/eventos").param("modalidad", "HACK"))
                .andExpect(status().isBadRequest());
    }

    // ---------- Rutas privadas ----------

    @Test
    void detalleSinTokenResponde401() throws Exception {
        mvc.perform(get("/api/eventos/1")).andExpect(status().isUnauthorized());
    }

    @Test
    void detalleConTokenAlteradoResponde401() throws Exception {
        String token = login("jugador1", "Gamer#2026");
        String alterado = token.substring(0, token.length() - 4) + "AAAA";
        mvc.perform(get("/api/eventos/1").header("Authorization", "Bearer " + alterado))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void detalleConTokenFirmadoPorOtraClaveResponde401() throws Exception {
        // Token con alg "none" (sin firma): debe rechazarse.
        String sinFirma = "eyJhbGciOiJub25lIn0.eyJzdWIiOiJqdWdhZG9yMSIsInNjb3BlIjoiZXZlbnRvcy5sZWVyIn0.";
        mvc.perform(get("/api/eventos/1").header("Authorization", "Bearer " + sinFirma))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void detalleConTokenValidoDevuelveTodosLosDatos() throws Exception {
        String token = login("jugador1", "Gamer#2026");
        mvc.perform(get("/api/eventos/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").isNotEmpty())
                .andExpect(jsonPath("$.videojuego", is("Valorant")))
                .andExpect(jsonPath("$.organizador.nombre").isNotEmpty())
                .andExpect(jsonPath("$.requisitos").isNotEmpty())
                .andExpect(jsonPath("$.infoInscripcion").isNotEmpty())
                .andExpect(jsonPath("$.fotos", hasSize(3)));
        mvc.perform(get("/api/eventos/9999").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void rutasNoDeclaradasSeRechazan() throws Exception {
        mvc.perform(get("/api/usuarios")).andExpect(status().isUnauthorized());
        mvc.perform(post("/api/publico/eventos")).andExpect(status().isUnauthorized());
    }

    // ---------- Login ----------

    @Test
    void loginIncorrectoRespondeMensajeGenerico() throws Exception {
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"jugador2\",\"password\":\"incorrecta\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail", is("Usuario o contraseña incorrectos.")));
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"noexiste\",\"password\":\"incorrecta\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail", is("Usuario o contraseña incorrectos.")));
    }

    @Test
    void loginSeBloqueaTrasCincoFallos() throws Exception {
        String malo = "{\"username\":\"usuario-bloqueo\",\"password\":\"x\"}";
        for (int i = 0; i < 5; i++) {
            mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(malo))
                    .andExpect(status().isUnauthorized());
        }
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(malo))
                .andExpect(status().isTooManyRequests());
    }

    // ---------- Login por formulario (user / encryptedPass) ----------

    @Test
    void loginFormularioDevuelveTokenQueAbreLaApiPrivada() throws Exception {
        String token = mvc.perform(post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("user", "jugador1").param("encryptedPass", "Gamer#2026"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn().getResponse().getContentAsString();
        mvc.perform(get("/api/eventos/1").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void loginFormularioIncorrectoOIncompletoSeRechazaSinDetallesInternos() throws Exception {
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("user", "jugador2").param("encryptedPass", "incorrecta"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail", is("Usuario o contraseña incorrectos.")));
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED).param("user", "jugador2"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(not(containsString("Exception"))));
    }
}
