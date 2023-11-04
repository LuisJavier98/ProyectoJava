package com.proyecto.Proyecto.Controller;

import com.proyecto.Proyecto.Model.Usuario;
import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Responses.Validations;
import com.proyecto.Proyecto.Service.UsuarioService;
import com.proyecto.Proyecto.Util.TokenGenerator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("api/usuario")
@AllArgsConstructor
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JavaMailSender javaMailSender;

    public ResponseEntity<?> enviarCorreo(String correo, String token) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(correo);
        email.setFrom("correodeprueba.2700@gmail.com");
        email.setSubject("Mensaje de prueba 1");
        email.setText(String.format("Ingrese al siguiente link para activar su cuenta http://localhost:8000/api/usuario/activarCuenta?token=%s", token));
        javaMailSender.send(email);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PatchMapping("/actualizarContraseña")
    public ResponseEntity<HashMap<String, Object>> actualizarContraseña(@RequestBody @NotNull @Valid HashMap<String, String> contraseñas, HttpServletRequest request) {
        Response respuesta = usuarioService.actualizarContraseña(contraseñas, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/actualizarUsuario")
    public ResponseEntity<HashMap<String, Object>> actualizarUsuario(@RequestBody @NotNull @Valid HashMap<String, Object> usuarioNuevo, HttpServletRequest request) {
        Response respuesta = usuarioService.actualizarUsuario(usuarioNuevo, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        }
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/activarCuenta")
    public ResponseEntity<HashMap<String, Object>> validarUsuario(@RequestParam("token") String token) {
        Response respuesta = usuarioService.habilitarUsuario(token);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        }
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public ResponseEntity<HashMap<String, Object>> crearUsuario(@RequestBody @Valid @NotNull Usuario usuario) {
        if (Objects.isNull(usuario.getEmail()))
            return new ResponseEntity<>(new Response("Introduce un correo electronico", true).mensajeResponse(), HttpStatus.BAD_REQUEST);
        if (new Validations(usuario.getEmail()).isValidEmail()) {
            String token = TokenGenerator.generateActivationToken();
            Response respuesta = usuarioService.crearUsuario(usuario, token);
            if (respuesta.getIsBadResponse()) {
                return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
            }
            enviarCorreo(usuario.getEmail(), token);
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new Response("Email no valido", true).mensajeResponse(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    public ResponseEntity<HashMap<String, Object>> usuarioLogin(@RequestBody HashMap<String, String> login) {
        Response respuesta = usuarioService.loginUsuario(login);
        if (!respuesta.getIsBadResponse()) return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.NOT_FOUND);

    }
}
