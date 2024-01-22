package com.proyecto.Proyecto;

import com.proyecto.Proyecto.Model.Roles;
import com.proyecto.Proyecto.Model.Usuario;
import com.proyecto.Proyecto.Repository.RolJpaRepository;
import com.proyecto.Proyecto.Repository.UsuarioJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitApplication implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    UsuarioJpaRepository usuarioJpaRepository;
    @Autowired
    private RolJpaRepository rolJpaRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        List<Roles> listaRoles = rolJpaRepository.findAll();
        if (!listaRoles.isEmpty()) return;
        Roles roles1 = Roles.builder().rol("ADMIN").build();
        Roles roles2 = Roles.builder().rol("USER").build();
        rolJpaRepository.save(roles1);
        rolJpaRepository.save(roles2);
        if (!usuarioJpaRepository.findAll().isEmpty()) return;
        List<Roles> user_roles = new ArrayList<>();
        System.out.println(rolJpaRepository.findById(1L).orElse(null));
        user_roles.add(rolJpaRepository.findById(1L).orElse(null));
        user_roles.add(rolJpaRepository.findById(2L).orElse(null));
        Usuario usuario1 = Usuario.builder().nombre("Luis").apellido("Burga").roles(user_roles).email("luisjavier_2705@hotmail.com").numero("+51999999999").contrasenia(passwordEncoder.encode("0147")).tokenValidacion(null).habilitado(true).build();
        usuarioJpaRepository.save(usuario1);
    }
}
