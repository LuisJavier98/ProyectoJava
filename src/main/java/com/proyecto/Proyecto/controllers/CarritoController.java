package com.proyecto.Proyecto.controllers;

import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Security.jwt.JwtUtil;
import com.proyecto.Proyecto.Service.CarritoService;
import com.proyecto.Proyecto.Swagger.Schemas.Cantidad;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/carrito")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/{id}")
    @Operation(summary = "Crea un producto")
    @SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la solicitud para la creación de un elemento",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Cantidad.class)))
    public ResponseEntity<HashMap<String, Object>> crearProducto_Usuario(@RequestBody @Valid @NotNull HashMap<String, Integer> cantidad, @PathVariable long id, HttpServletRequest request) throws Exception {
        Response respuesta = carritoService.crearProductoCarrito(cantidad, id, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los productos del carrito ")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<HashMap<String, Object>>> obtenerProductoPorUsuarioId(HttpServletRequest request) {
        return new ResponseEntity<>(carritoService.obtenerProductos_Usuario(request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto del carrito")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<HashMap<String, Object>> eliminarProductoDeCarrito(@PathVariable Long id, HttpServletRequest request) throws Exception {
        Response respuesta = carritoService.eliminarProductoDelCarrito(id, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualiza la cantidad de productos del carrito")
    @SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la solicitud para la creación de un elemento",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Cantidad.class)))
    public ResponseEntity<HashMap<String, Object>> actualizarCantidadCarrito(@RequestBody HashMap<String, Integer> cantidad, @PathVariable long id, HttpServletRequest request) throws Exception {
        Response respuesta = carritoService.actualizarCantidadDelCarrito(cantidad, id, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }
    }
}
