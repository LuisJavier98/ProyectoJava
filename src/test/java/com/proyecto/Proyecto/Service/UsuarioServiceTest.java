package com.proyecto.Proyecto.Service;

import com.proyecto.Proyecto.Repository.UsuarioJpaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest {
    @Mock
    UsuarioJpaRepository usuarioJpaRepository;
    @InjectMocks
    UsuarioService usuarioService;

    @Test
    void actualizarUsuario() {
    }

    @Test
    void crearUsuario() {
    }

    @Test
    void loginUsuario() {
    }

    @Test
    void actualizarContraseña() {
    }
}