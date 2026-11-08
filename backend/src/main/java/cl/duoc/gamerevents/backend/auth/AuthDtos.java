package cl.duoc.gamerevents.backend.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AuthDtos {

    private AuthDtos() {
    }

    public record LoginRequest(
            @NotBlank @Size(max = 50) String username,
            @NotBlank @Size(max = 100) String password) {

        /** Evita que la contraseña aparezca si el objeto se imprime en un log. */
        @Override
        public String toString() {
            return "LoginRequest[username=" + username + ", password=****]";
        }
    }

    public record LoginResponse(String token, String tipo, long expiraEnSegundos, String nombre, String rol) {
    }
}
