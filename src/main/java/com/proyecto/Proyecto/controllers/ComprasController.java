package com.proyecto.Proyecto.controllers;

import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Service.ComprasService;
import com.proyecto.Proyecto.Swagger.Schemas.Direccion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {
    @Autowired
    ComprasService comprasService;

    @PostMapping
    @Operation(summary = "Finaliza la compra de los productos del carrito")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Cuerpo de la solicitud para la creación de un elemento",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = Direccion.class)))
    public ResponseEntity<HashMap<String, Object>> finalizarCompra(HttpServletRequest request, @RequestBody @NotNull HashMap<String, String> direccion) throws Exception {
        Response respuesta = comprasService.compraFinalizada(request, direccion);
        if (respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
    }
    @GetMapping
    @Operation(summary = "Obtiene todas las compras realizadas")

    public ResponseEntity<List<HashMap<String, Object>>> obtenerCompras(HttpServletRequest request){
        List<HashMap<String, Object>> respuesta = comprasService.obtenerCompras(request);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

}
