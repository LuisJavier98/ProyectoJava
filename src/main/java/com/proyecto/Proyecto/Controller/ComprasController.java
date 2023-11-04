package com.proyecto.Proyecto.Controller;

import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Service.ComprasService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/api/compras")
public class ComprasController {
    @Autowired
    ComprasService comprasService;

    @PostMapping
    public ResponseEntity<HashMap<String, Object>> finalizarCompra(HttpServletRequest request, @RequestBody @NotNull HashMap<String, String> direccion) throws Exception {
        Response respuesta = comprasService.compraFinalizada(request, direccion);
        if (respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
    }
}
