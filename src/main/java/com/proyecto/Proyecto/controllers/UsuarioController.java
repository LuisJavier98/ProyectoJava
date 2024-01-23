package com.proyecto.Proyecto.controllers;

import com.proyecto.Proyecto.Model.Usuario;
import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Responses.Validations;
import com.proyecto.Proyecto.Service.UsuarioService;
import com.proyecto.Proyecto.Swagger.Schemas.ActualizarContraseña;
import com.proyecto.Proyecto.Swagger.Schemas.Login;
import com.proyecto.Proyecto.Swagger.Schemas.NuevoUsuario;
import com.proyecto.Proyecto.Util.TokenGenerator;
import com.proyecto.Proyecto.Util.UsuarioActualizado;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("api/usuario")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JavaMailSender javaMailSender;

    public ResponseEntity<?> enviarCorreo(String correo, String token, HttpServletRequest request) {
        String domain = request.getScheme() + "://" + request.getServerName();
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(correo);
        email.setFrom("correodeprueba.2700@gmail.com");
        email.setSubject("Mensaje de verificacion de cuenta");
        email.setText(String.format("Ingrese al siguiente link para activar su cuenta " + "%s/api/usuario/activarCuenta?token=%s", domain, token));
        javaMailSender.send(email);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PatchMapping("/actualizarContraseña")
    @Operation(summary = "Actualiza la contraseña del usuario")
    @SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la solicitud para la creación de un elemento", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActualizarContraseña.class)))
    public ResponseEntity<HashMap<String, Object>> actualizarContraseña(@RequestBody @NotNull @Valid HashMap<String, String> contraseñas, HttpServletRequest request) {
        Response respuesta = usuarioService.actualizarContraseña(contraseñas, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/actualizarUsuario")
    @Operation(summary = "Actualiza el usuario")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<HashMap<String, Object>> actualizarUsuario(@RequestBody @NotNull @Valid UsuarioActualizado usuarioActualizado, HttpServletRequest request) {
        Response respuesta = usuarioService.actualizarUsuario(usuarioActualizado, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        }
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);

    }

    @GetMapping("/activarCuenta")
    @Operation(summary = "Validación del usuario")

    public ResponseEntity<HashMap<String, Object>> validarUsuario(@RequestParam("token") String token) {
        Response respuesta = usuarioService.habilitarUsuario(token);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        }
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    @Operation(summary = "Crea un nuevo usuario")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la solicitud para la creación de un elemento", content = @Content(mediaType = "application/json", schema = @Schema(implementation = NuevoUsuario.class)))
    public ResponseEntity<HashMap<String, Object>> crearUsuario(@RequestBody(required = true) @Valid Usuario usuario, HttpServletRequest request) {
        if (Objects.isNull(usuario.getEmail()))
            return new ResponseEntity<>(new Response("Introduce un correo electronico", true).mensajeResponse(), HttpStatus.BAD_REQUEST);
        if (new Validations(usuario.getEmail()).isValidEmail()) {
            String token = TokenGenerator.generateActivationToken();
            Response respuesta = usuarioService.crearUsuario(usuario, token);
            if (respuesta.getIsBadResponse()) {
                return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
            }
            enviarCorreo(usuario.getEmail(), token, request);
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new Response("Email no valido", true).mensajeResponse(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/login")
    @Operation(summary = "Loggeo del usuario")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la solicitud para la creación de un elemento", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Login.class)))
    public ResponseEntity<HashMap<String, Object>> usuarioLogin(@RequestBody HashMap<String, String> login) {
        Response respuesta = usuarioService.loginUsuario(login);
        if (!respuesta.getIsBadResponse()) return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.NOT_FOUND);

    }
}
