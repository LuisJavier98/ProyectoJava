package com.proyecto.Proyecto.Service;

import com.proyecto.Proyecto.Model.Carrito;
import com.proyecto.Proyecto.Model.Compras;
import com.proyecto.Proyecto.Model.Producto;
import com.proyecto.Proyecto.Repository.CarritoJpaRepository;
import com.proyecto.Proyecto.Repository.ComprasJpaRepository;
import com.proyecto.Proyecto.Repository.ProductoJpaRepository;
import com.proyecto.Proyecto.Responses.Response;
import com.proyecto.Proyecto.Security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ComprasService {
    @Autowired
    private ComprasJpaRepository comprasJpaRepository;
    @Autowired
    private CarritoJpaRepository carritoJpaRepository;
    @Autowired
    private ProductoJpaRepository productoJpaRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public Response compraFinalizada(HttpServletRequest request, HashMap<String, String> direccion) throws Exception {
        String token = request.getHeader("Authorization").split(" ")[1];
        List<Carrito> carrito = carritoJpaRepository.findAllByUsuario_id(jwtUtil.getUserId(token)).orElse(null);
        List<Compras> compras = new ArrayList<>();
        List<Producto> productos = new ArrayList<>();
        if (carrito.size() == 0) {
            return new Response("El carrito esta vacio", true);
        }
        try {
            carrito.stream().forEach(carrito1 -> {
                Producto producto = productoJpaRepository.findById(carrito1.getProducto().getId()).orElse(null);
                if (carrito1.getCantidad() <= producto.getUnidades() && carrito1.getCantidad() > 0) {
                    producto.setUnidades(producto.getUnidades() - carrito1.getCantidad());
                    Compras compra = new Compras();
                    compra.setDireccion(direccion.get("direccion"));
                    compra.setUsuario(carrito1.getUsuario());
                    compra.setProducto(carrito1.getProducto());
                    compra.setFechaCompra(new Date());
                    compras.add(compra);
                    productos.add(producto);
                } else if (carrito1.getCantidad() <= 0) {
                    throw new RuntimeException(String.format("La cantidad de %ss debe ser mayor a 0", carrito1.getProducto().getNombre()));
                } else {
                    throw new RuntimeException(String.format("La cantidad de %ss que intentas comprar es mayor al stock disponible , por favor actualice la pagina", carrito1.getProducto().getNombre()));

                }
            });
            productoJpaRepository.saveAll(productos);
            comprasJpaRepository.saveAll(compras);
            carritoJpaRepository.deleteAllByUsuario_id(jwtUtil.getUserId(token));
            return new Response("Compra finalizada", false);
        } catch (Exception e) {
            return new Response(e.getMessage(), true);
        }
    }
}