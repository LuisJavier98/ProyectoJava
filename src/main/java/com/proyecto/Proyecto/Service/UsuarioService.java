package com.proyecto.Proyecto.Service;

import com.proyecto.Proyecto.Model.Roles;
import com.proyecto.Proyecto.Model.Usuario;
import com.proyecto.Proyecto.Repository.RolJpaRepository;
import com.proyecto.Proyecto.Repository.UsuarioJpaRepository;
import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Security.CustomDetailsServices;
import com.proyecto.Proyecto.Security.jwt.JwtUtil;
import com.proyecto.Proyecto.Util.UsuarioActualizado;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class UsuarioService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;
    @Autowired
    private CustomDetailsServices customDetailsServices;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RolJpaRepository rolJpaRepository;

    public Response actualizarUsuario(UsuarioActualizado usuarioActualizado, HttpServletRequest request) {
        String token = request.getHeader("Authorization").split(" ")[1];
        Usuario usuario = usuarioJpaRepository.findById(jwtUtil.getUserId(token)).orElse(null);
        Usuario usuarioNumero = usuarioJpaRepository.findByNumero(usuarioActualizado.getNumero()).orElse(null);
        Usuario usuarioNumeroId = usuarioJpaRepository.findAllByNumeroAndId(usuarioActualizado.getNumero(), jwtUtil.getUserId(token)).orElse(null);
        if (!Objects.isNull(usuarioActualizado.getNombre())) usuario.setNombre(usuarioActualizado.getNombre());
        if (!Objects.isNull(usuarioActualizado.getApellido())) usuario.setApellido(usuarioActualizado.getApellido());
        if (!Objects.isNull(usuarioActualizado.getNumero()) && Objects.isNull(usuarioNumero)) {
            System.out.println(usuarioActualizado.getNumero());
            if (!(usuarioActualizado.getNumero() >= 900000000 && usuarioActualizado.getNumero() <= 999999999L))
                return new Response("Introduce un numero de celular valido", true);
            usuario.setNumero(String.valueOf(usuarioActualizado.getNumero()));
        } else if (!Objects.isNull(usuarioActualizado.getNumero()) && !Objects.isNull(usuarioNumeroId)) {
            return new Response("Introduce un numero distinto al anterior", true);
        } else if (!Objects.isNull(usuarioActualizado.getNumero())) {
            return new Response("El numero ya se encuentra en uso", true);
        }
        usuarioJpaRepository.save(usuario);
        return new Response("Usuario actualizado correctamente", false);
    }

    public Response crearUsuario(@Valid Usuario usuario, String token) {
//        if (usuario.getNumero() == 0) return new Response("Introduce tu numero de celular", true);
//        if (!(usuario.getNumero() > 900000000L && usuario.getNumero() <= 999999999L))
//            return new Response("Introduce un numero de celular valido", true);
//        if (Objects.isNull(usuario.getApellido())) return new Response("Introduce tu apellido", true);
//        if (Objects.isNull(usuario.getNombre())) return new Response("Introduce tu nombre", true);
//        if (Objects.isNull(usuario.getContrasenia())) return new Response("Introduce tu contraseña", true);
        Usuario email = usuarioJpaRepository.findByEmail(usuario.getEmail()).orElse(null);
        Usuario number = usuarioJpaRepository.findByNumero(usuario.getNumero()).orElse(null);
        if (!Objects.isNull(email))
            return new Response("El email que introdujo ya se encuentra en uso , por favor intente nuevamente", true);
        if (!Objects.isNull(number))
            return new Response("El numero que introdujo ya se encuentra en uso , por favor intente nuevamente", true);
        Roles rol = rolJpaRepository.findById(2L).orElse(null);
        if (Objects.isNull(rol)) return new Response("El rol no existe", true);

        usuario.setNumero(usuario.getNumero());
        usuario.getRoles().add(rol);
        usuario.setContrasenia(passwordEncoder.encode(usuario.getContrasenia()));
        usuario.setHabilitado(false);
        usuario.setTokenValidacion(token);
        usuarioJpaRepository.save(usuario);
        return new Response("Usuario creado satisfactoriamente,por favor revise la bandeja de su correo", false);

    }

    public Response habilitarUsuario(String token) {
        Usuario usuario = usuarioJpaRepository.findByTokenValidacion(token);
        if (!Objects.isNull(usuario)) {
            usuario.setHabilitado(true);
            usuario.setTokenValidacion(null);
            usuarioJpaRepository.save(usuario);
            return new Response("Validación de cuenta exitosa", false);
        }
        return new Response("El token ya fue usado", true);
    }

    public Response loginUsuario(HashMap<String, String> login) {
        if (Objects.isNull(login.get("email"))) return new Response("Introduce tu email", true);
        if (Objects.isNull(login.get("contraseña"))) return new Response("Introduce tu contraseña", true);
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.get("email"), login.get("contraseña")));
            if (authentication.isAuthenticated()) {
                return new Response("Usuario autenticado correctamente", jwtUtil.generateToken(customDetailsServices.getUserDetail().getEmail(), customDetailsServices.getUserDetail().getId()), "token", false);
            } else {
                return new Response("El usuario no está autenticado", true);
            }
        } catch (Exception e) {
            return new Response("Usuario no encontrado", true);
        }
    }

    public Response actualizarContraseña(HashMap<String, String> contraseñas, HttpServletRequest request) {
        if (Objects.isNull(contraseñas.get("contraseña"))) return new Response("Introduce la contraseña actual", true);
        if (Objects.isNull(contraseñas.get("contraseñaNueva")))
            return new Response("Introduce la contraseña nueva", true);

        String token = request.getHeader("Authorization").split(" ")[1];
        Usuario usuario = usuarioJpaRepository.findById(jwtUtil.getUserId(token)).orElse(null);
        String contraseñaAnterior = usuario.getContrasenia();
        if (passwordEncoder.matches(contraseñas.get("contraseña"), contraseñaAnterior)) {
            if (contraseñas.get("contraseñaNueva").equals(contraseñas.get("contraseña"))) {
                return new Response("Las contraseñas deben ser distintas", true);
            }
            usuario.setContrasenia(passwordEncoder.encode(contraseñas.get("contraseñaNueva")));
            usuarioJpaRepository.save(usuario);
            return new Response("Contraseña modificada correctamente", true);
        } else {
            return new Response("Las contraseñas no coinciden", true);
        }
    }
}
