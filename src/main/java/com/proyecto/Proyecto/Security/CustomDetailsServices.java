package com.proyecto.Proyecto.Security;

import com.proyecto.Proyecto.Model.Roles;
import com.proyecto.Proyecto.Model.Usuario;
import com.proyecto.Proyecto.Repository.UsuarioJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CustomDetailsServices implements UserDetailsService {

    @Autowired
    private UsuarioJpaRepository usuarioJpaRepository;

    private Usuario usuarioDetail;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info(username);
        usuarioDetail = usuarioJpaRepository.findByEmail(username).orElse(null);
        if (!Objects.isNull(usuarioDetail)) {
            System.out.println("entraa");
            System.out.println(mapRolesToAuthorities(usuarioDetail.getRoles()));
            return new User(usuarioDetail.getEmail(), usuarioDetail.getContrasenia(), usuarioDetail.getHabilitado(), true, true, true, mapRolesToAuthorities(usuarioDetail.getRoles()));
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Roles> roles) {
        List<GrantedAuthority> lista = new ArrayList<>();
        for (Roles rol : roles) {
            lista.add(new SimpleGrantedAuthority("ROLE_" + rol.getRol()));
        }
        return lista;
    }

    public Usuario getUserDetail() {
        return usuarioDetail;
    }
}
