package com.proyecto.Proyecto.Repository;


import com.proyecto.Proyecto.Model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioJpaRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Usuario findByTokenValidacion(String token);

    Optional<Usuario> findByNumero(Object numero);

    Optional<Usuario> findAllByNumeroAndId(Object numero, Long id);
}
