package com.proyecto.Proyecto.Service;

import com.proyecto.Proyecto.Model.Producto;
import com.proyecto.Proyecto.Repository.ProductoJpaRepository;
import com.proyecto.Proyecto.Responses.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class ProductoService {
    @Autowired
    private ProductoJpaRepository productoJpaRepository;


    public Optional<Producto> obtenerProductoById(long id) {
        Optional<Producto> producto = productoJpaRepository.findById(id);
        return producto;
    }

    public List<Producto> obtenerProductos(HttpServletRequest request) {
        String domain = request.getScheme() + "://" + request.getServerName();
        int port = request.getLocalPort();
        List<Producto> productos = productoJpaRepository.findAll();
        productos = productos.stream().map(producto -> {
            producto.setImagen(domain + ":" + port + "/images" + producto.getImagen());
            return producto;
        }).collect(Collectors.toList());
        return productos;
    }

    public Response crearProducto(MultipartFile imagen, String nombre, String precio, String categoria, String descripcion, String unidades) throws IOException {
        Producto productoIsAlreadyExist = productoJpaRepository.findByImagen("/" + imagen.getOriginalFilename()).orElse(null);
        if (!Objects.isNull(productoIsAlreadyExist)) return new Response("El producto ya fue creado", true);
        try {
            String rutaAbsoluta = Paths.get("images").toString();
            File directorioDeGuardado = new File(new File(rutaAbsoluta).getAbsolutePath());
            File savedFile = new File(directorioDeGuardado, imagen.getOriginalFilename());
            imagen.transferTo(savedFile);
            Producto producto = new Producto();
            producto.setImagen(String.format("/%s", imagen.getOriginalFilename().replaceAll(" ", "%20")));
            producto.setPrecio(Integer.parseInt(precio));
            producto.setNombre(nombre);
            producto.setCategoria(categoria);
            producto.setDescripcion(descripcion);
            producto.setUnidades(Integer.parseInt(unidades));
            productoJpaRepository.save(producto);
            return new Response("Producto creado satisfatoriamente", false);
        } catch (Exception e) {
            return new Response("La ruta no existe", true);
        }
    }

    public Response eliminarProducto(Long id) throws IOException {
        Producto producto = productoJpaRepository.findById(id).orElse(null);
        if (Objects.isNull(producto)) return new Response("El producto no existe", true);
        String ruta = "images" + producto.getImagen().replace("%20", " ");
        Path path = Paths.get(ruta);
        Files.delete(path);
        productoJpaRepository.deleteById(id);
        return new Response("El producto fue eliminado correctamente", false);
    }

    public Response actualizarProducto(Long id, MultipartFile imagen, String nombre, String precio, String categoria, String descripcion, String unidades) throws IOException {
        Producto producto = productoJpaRepository.findById(id).orElse(null);
        if (Objects.isNull(producto)) return new Response("El producto no existe", true);
        if (!Objects.isNull(imagen) && !producto.getImagen().equals("/"+imagen.getOriginalFilename())) {
            //ELIMINAR PRODUCTO DE IMAGENES
            String ruta = "images" + producto.getImagen().replace("%20", " ");
            Path path = Paths.get(ruta);
            Files.delete(path);
            //AGREGAR PRODUCTO A IMAGENES
            String rutaAbsoluta = Paths.get("images").toString();
            File directorioDeGuardado = new File(new File(rutaAbsoluta).getAbsolutePath());
            File savedFile = new File(directorioDeGuardado, imagen.getOriginalFilename());
            imagen.transferTo(savedFile);
            //AGREGAR A BD
            producto.setImagen(String.format("/%s", imagen.getOriginalFilename().replaceAll(" ", "%20")));
        }
        if (!Objects.isNull(nombre)) producto.setNombre(nombre);
        if (!Objects.isNull(precio)) producto.setPrecio(Integer.parseInt(precio));
        if (!Objects.isNull(categoria)) producto.setCategoria(categoria);
        if (!Objects.isNull(descripcion)) producto.setDescripcion(descripcion);
        if (!Objects.isNull(unidades)) producto.setUnidades(Integer.parseInt(unidades));
        productoJpaRepository.save(producto);
        return new Response("Producto actualizado correctamente", false);
    }
}
