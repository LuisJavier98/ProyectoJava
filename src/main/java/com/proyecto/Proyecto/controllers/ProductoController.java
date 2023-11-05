package com.proyecto.Proyecto.controllers;
import com.proyecto.Proyecto.Model.Producto;
import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/productos")
public class ProductoController {

    private ProductoService productoService;

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un producto especifico por id")


    public ResponseEntity<?> obtenerProducto(@PathVariable Long id, HttpServletRequest request) {
        Producto producto = productoService.obtenerProductoById(id, request);
        if (!Objects.isNull(producto)) {
            return new ResponseEntity<>(producto, HttpStatus.OK);
        } HashMap<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Producto no encontrado");
        return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @Operation(summary = "Obtiene todos los productos")

    public ResponseEntity<List<Producto>> obtenerProductos(HttpServletRequest request) {
        List<Producto> productos = productoService.obtenerProductos(request);
        return new ResponseEntity<>(productos, HttpStatus.OK);
    }

    @PostMapping("/crearProducto")
    @Operation(summary = "Crea un producto nuevo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HashMap<String, Object>> crearProducto(@NotNull @RequestParam("file") MultipartFile file, @NotNull @RequestParam("nombre") String nombre, @NotNull @RequestParam("precio") String precio, @NotNull @RequestParam("categoria") String categoria, @NotNull @RequestParam("descripcion") String descripcion, @NotNull @RequestParam("unidades") String unidades) throws IOException {
        Response respuesta = productoService.crearProducto(file, nombre, precio, categoria, descripcion, unidades);
        if (respuesta.getIsBadResponse())
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);

    }

    @DeleteMapping("/eliminarProducto/{id}")
    @Operation(summary = "Eliminar un producto determinado")

    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HashMap<String, Object>> eliminarProducto(@NotNull @PathVariable Long id) throws IOException {
        Response respuesta = productoService.eliminarProducto(id);
        if (respuesta.getIsBadResponse())
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
    }

    @PatchMapping("/actualizarProducto/{id}")
    @Operation(summary = "Actualiza un producto determinado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HashMap<String, Object>> actualizarProducto(@NotNull @PathVariable Long id, @RequestParam(value = "file", required = false) MultipartFile file, @RequestParam(value = "nombre", required = false) String nombre, @RequestParam(value = "precio", required = false) String precio, @RequestParam(value = "categoria", required = false) String categoria, @RequestParam(value = "descripcion", required = false) String descripcion, @RequestParam(value = "unidades", required = false) String unidades) throws IOException {
        Response respuesta = productoService.actualizarProducto(id, file, nombre, precio, categoria, descripcion, unidades);
        if (respuesta.getIsBadResponse())
            return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(respuesta.mensajeResponse(), HttpStatus.OK);
    }

}
