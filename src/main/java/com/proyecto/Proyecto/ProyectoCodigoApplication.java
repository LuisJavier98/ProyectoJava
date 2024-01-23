package com.proyecto.Proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
@EnableMethodSecurity
@EnableJpaAuditing
public class ProyectoCodigoApplication implements WebMvcConfigurer {
    public static void main(String[] args) {
        SpringApplication.run(ProyectoCodigoApplication.class, args);
    }
}

