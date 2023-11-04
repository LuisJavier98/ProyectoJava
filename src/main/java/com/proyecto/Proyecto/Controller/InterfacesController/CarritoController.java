package com.proyecto.Proyecto.Controller.InterfacesController;

import com.proyecto.Proyecto.Model.Carrito;
import com.proyecto.Proyecto.Model.Producto;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

public interface CarritoController {
@ApiResponses(value = {
        @ApiResponse(responseCode = "200",description = "Ok",content = {
                @Content(mediaType = "application/json",schema = @Schema(implementation = List.class))
        })
})
@ApiOperation(
        value = "The service returns products by user id" ,
        response = List.class,
        responseContainer = "List<Producto>"
)
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerProducto_Usuario(HttpServletRequest request);
}
