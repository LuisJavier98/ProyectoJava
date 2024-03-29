package com.proyecto.Proyecto.controllers;

import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Service.ComprasService;
import com.proyecto.Proyecto.controllers.InterfaceControllers.ComprasControllerInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;


@RestController
public class ComprasController implements ComprasControllerInterface {
    @Autowired
    ComprasService comprasService;

    public ResponseEntity<HashMap<String, Object>> finalizarCompra(
            HttpServletRequest request,
            @RequestBody @NotNull HashMap<String, String> direccion) throws Exception {
        Response respuesta = comprasService.compraFinalizada(request, direccion);
        if (respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
    }

    public ResponseEntity<List<HashMap<String, Object>>> obtenerCompras(HttpServletRequest request){
        List<HashMap<String, Object>> respuesta = comprasService.obtenerCompras(request);
        return new ResponseEntity<>(respuesta, HttpStatus.OK);
    }

}
