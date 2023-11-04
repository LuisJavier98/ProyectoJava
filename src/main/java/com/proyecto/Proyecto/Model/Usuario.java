package com.proyecto.Proyecto.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String apellido;
    @Column(nullable = false)
    @JsonProperty("contraseña")
    private String contrasenia;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    @Size(min = 9, max = 9, message = "El número debe tener entre 9 digitos")
    private long numero;
    @ManyToOne
    private Roles rol;
    private Boolean habilitado;
    private String tokenValidacion;
    private Date creado;
    private Date actualizado;
}

