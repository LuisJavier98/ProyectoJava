package com.proyecto.Proyecto.controllers.InterfaceControllers;

import com.proyecto.Proyecto.Swagger.Schemas.Direccion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/compras")
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, scheme = "bearer")
public interface ComprasControllerInterface {

    @PostMapping
    @Operation(summary = "Finaliza la compra de los productos del carrito")
    @SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la solicitud para la creación de un elemento",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Direccion.class)))
    public ResponseEntity<HashMap<String, Object>> finalizarCompra(HttpServletRequest request, @RequestBody @NotNull HashMap<String, String> direccion) throws Exception;

    @GetMapping
    @Operation(summary = "Obtiene todas las compras realizadas")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<HashMap<String, Object>>> obtenerCompras(HttpServletRequest request);



}
