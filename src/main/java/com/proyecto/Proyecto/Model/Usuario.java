package com.proyecto.Proyecto.Model;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "usuario")
public class Usuario extends Auditing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    @NotBlank(message = "Introduzca el nombre")
    private String nombre;
    @Column(nullable = false)
    @NotBlank(message = "Introduzca el apellido")
    private String apellido;
    @Column(nullable = false)
    @JsonProperty("contraseña")
    @NotBlank(message = "Introduzca la contraseña")
    private String contrasenia;
    @Column(unique = true, nullable = false)
    @Email(message = "Introduzca un email valido")
    private String email;
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Introduzca el numero de telefono")
    @Pattern(regexp ="^(\\+51|0051|51)?[9]\\d{8}$" ,message = "Introduzca un numero valido")
    private String numero;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL )
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private List<Roles> roles = new ArrayList<Roles>();
    private Boolean habilitado;
    private String tokenValidacion;
}

