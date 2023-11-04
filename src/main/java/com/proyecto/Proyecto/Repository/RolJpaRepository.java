package com.proyecto.Proyecto.Repository;

import com.proyecto.Proyecto.Model.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolJpaRepository extends JpaRepository<Roles, Long> {
}
