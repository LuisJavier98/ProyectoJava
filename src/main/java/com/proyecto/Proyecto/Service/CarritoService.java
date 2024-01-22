package com.proyecto.Proyecto.Service;

import com.proyecto.Proyecto.Model.Carrito;
import com.proyecto.Proyecto.Model.Producto;
import com.proyecto.Proyecto.Model.Usuario;
import com.proyecto.Proyecto.Repository.CarritoJpaRepository;
import com.proyecto.Proyecto.Repository.ProductoJpaRepository;
import com.proyecto.Proyecto.Repository.UsuarioJpaRepository;
import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CarritoService {
    @Autowired
    private CarritoJpaRepository carritoJpaRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;
    @Autowired
    private ProductoJpaRepository productoJpaRepository;

    public Response crearProductoCarrito(HashMap<String, Integer> cantidad, Long id, HttpServletRequest request) {

        String token = request.getHeader("Authorization").split(" ")[1];
        Carrito productAlreadyExist = carritoJpaRepository.findByProducto_idAndUsuario_id(id, jwtUtil.getUserId(token));
        Producto producto = productoJpaRepository.findById(id).orElse(null);
        if (Objects.isNull(productAlreadyExist)) {
            if (!Objects.isNull(producto)) {
                Usuario usuario = usuarioJpaRepository.findById(jwtUtil.getUserId(token)).orElse(null);
                if (cantidad.get("cantidad") <= producto.getUnidades() && cantidad.get("cantidad") > 0) {
                    Carrito carrito = new Carrito(usuario, producto, cantidad.get("cantidad"));
                    carritoJpaRepository.save(carrito);
                    return new Response("Producto agregado satisfactoriamente", false);
                } else if (cantidad.get("cantidad") <= 0) {
                    return new Response("La cantidad de productos debe ser mayor a 0", true);

                } else {
                    return new Response("No exiten unidades suficientes", true);
                }
            } else {
                return new Response("El producto no existe", true);
            }
        } else {
            return new Response("El producto ya existe en el carrito", true);
        }

    }

    public List<HashMap<String, Object>> obtenerProductos_Usuario(HttpServletRequest request) {

        String token = request.getHeader("Authorization").split(" ")[1];
        List<Carrito> carritos = carritoJpaRepository.findAllByUsuario_id(jwtUtil.getUserId(token)).orElse(null);
        if (!Objects.isNull(carritos)) {
            String domain = request.getScheme() + "://" + request.getServerName();
            int port = request.getLocalPort();
            List<HashMap<String, Object>> respuesta = carritos.stream().map(carrito -> {
                HashMap<String, Object> temp = new HashMap<>();
                temp.put("id",carrito.getProducto().getId());
                temp.put("nombre", carrito.getProducto().getNombre());
                temp.put("categoria", carrito.getProducto().getCategoria());
                temp.put("descripción", carrito.getProducto().getDescripcion());
                temp.put("precio", carrito.getProducto().getPrecio());
                temp.put("imagen", domain + ":" + port + "/images" + carrito.getProducto().getImagen());
                temp.put("cantidad", carrito.getCantidad());
                return temp;
            }).collect(Collectors.toList());
            return respuesta;
        }
        return new ArrayList<>();
    }

    @Transactional
    public Response eliminarProductoDelCarrito(Long id_producto, HttpServletRequest request) {
        String token = request.getHeader("Authorization").split(" ")[1];
        Carrito carrito = carritoJpaRepository.findByProducto_idAndUsuario_id(id_producto, jwtUtil.getUserId(token));
        if (!Objects.isNull(carrito)) {
            carritoJpaRepository.deleteByProductoIdAndUsuarioId(id_producto, jwtUtil.getUserId(token));
            return new Response("Producto eliminado del carrito", false);
        } else {
            return new Response("El producto no se encuentra en el carrito", true);
        }
    }


    public Response actualizarCantidadDelCarrito(HashMap<String, Integer> cantidad, Long id, HttpServletRequest request) {
        if (Objects.isNull(cantidad.get("cantidad"))) return new Response("Introduce la cantidad de unidades a comprar", true);
        String token = request.getHeader("Authorization").split(" ")[1];
        Carrito carrito = carritoJpaRepository.findByProducto_idAndUsuario_id(id, jwtUtil.getUserId(token));
        Producto producto = productoJpaRepository.findById(id).orElse(null);
        if (!Objects.isNull(carrito)) {
            if (cantidad.get("cantidad") <= producto.getUnidades() && cantidad.get("cantidad") > 0) {
                carrito.setCantidad(cantidad.get("cantidad"));
                carritoJpaRepository.save(carrito);
                return new Response("Producto modificado exitosamente", false);
            } else if (cantidad.get("cantidad") <= 0) {
                return new Response("La cantidad de productos debe ser mayor a 0", true);
            } else {
                return new Response("No existen unidades suficientes", true);

            }
        } else {
            return new Response("El producto no se encuentra en el carrito", true);
        }
    }
}
