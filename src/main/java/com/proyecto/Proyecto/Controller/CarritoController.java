package com.proyecto.Proyecto.Controller;

import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Security.jwt.JwtUtil;
import com.proyecto.Proyecto.Service.CarritoService;
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
public class CarritoController {

    @Autowired
    private CarritoService carritoService;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> crearProducto_Usuario(@RequestBody @Valid @NotNull HashMap<String, Integer> cantidad, @PathVariable long id, HttpServletRequest request) throws Exception {
        Response respuesta = carritoService.crearProductoCarrito(cantidad, id, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<HashMap<String, Object>>> obtenerProductoPorUsuarioId(HttpServletRequest request) {
        return new ResponseEntity<>(carritoService.obtenerProductos_Usuario(request), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> eliminarProductoDeCarrito(@PathVariable Long id, HttpServletRequest request) throws Exception {
        Response respuesta = carritoService.eliminarProductoDelCarrito(id, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/{id}")
    public ResponseEntity<HashMap<String, Object>> actualizarCantidadCarrito(@RequestBody HashMap<String, Integer> cantidad, @PathVariable long id, HttpServletRequest request) throws Exception {
        Response respuesta = carritoService.actualizarCantidadDelCarrito(cantidad, id, request);
        if (!respuesta.getIsBadResponse()) {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        }
    }
}
