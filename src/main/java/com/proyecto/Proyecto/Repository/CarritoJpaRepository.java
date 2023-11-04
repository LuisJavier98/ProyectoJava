package com.proyecto.Proyecto.Repository;

import com.proyecto.Proyecto.Model.Carrito;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarritoJpaRepository extends JpaRepository<Carrito, Long> {

    Optional<List<Carrito>> findAllByUsuario_id(long id);

    void deleteAllByUsuario_id(long id);

    Carrito findByProducto_id(long id);

    @Transactional
    void deleteByProductoIdAndUsuarioId(Long productoId, Long usuarioId);

    @Query("SELECT u FROM Carrito u WHERE u.producto.id=:producto_id AND u.usuario.id=:usuario_id")
    Carrito findByProducto_idAndUsuario_id(@Param("producto_id") Long productoId, @Param("usuario_id") Long usuarioId);

}
