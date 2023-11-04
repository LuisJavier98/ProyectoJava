package com.proyecto.Proyecto.Repository;

import com.proyecto.Proyecto.Model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductoJpaRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByImagen(String imagen);
}
