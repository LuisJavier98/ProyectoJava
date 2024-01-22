package com.proyecto.Proyecto.controllers.InterfaceControllers;

import com.proyecto.Proyecto.Swagger.Schemas.Cantidad;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/carrito")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
public interface CarritoControllerInterface {

    @PostMapping("/{id}")
    @Operation(summary = "Crea un producto")
    @SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la solicitud para la creación de un elemento", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cantidad.class)))
    ResponseEntity<HashMap<String, Object>> crearProducto_Usuario(@RequestBody @Valid @NotNull HashMap<String, Integer> cantidad, @PathVariable long id, HttpServletRequest request) throws Exception;

    @GetMapping
    @Operation(summary = "Obtiene todos los productos del carrito ")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<List<HashMap<String, Object>>> obtenerProductoPorUsuarioId(HttpServletRequest request);

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto del carrito")
    @SecurityRequirement(name = "bearerAuth")
    ResponseEntity<HashMap<String, Object>> eliminarProductoDeCarrito(
            @PathVariable Long id,
            HttpServletRequest request) throws Exception;

    @PatchMapping("/{id}")
    @Operation(summary = "Actualiza la cantidad de productos del carrito")
    @SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Cuerpo de la solicitud para la creación de un elemento",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Cantidad.class)))
    ResponseEntity<HashMap<String, Object>> actualizarCantidadCarrito(@RequestBody HashMap<String, Integer> cantidad, @PathVariable long id, HttpServletRequest request) throws Exception;
}

