package com.proyecto.Proyecto.Repository;

import com.proyecto.Proyecto.Model.Compras;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprasJpaRepository extends JpaRepository<Compras,Long> {
}
