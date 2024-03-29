package com.proyecto.Proyecto.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "producto")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Column(unique = true)
    private String nombre;
    @NotNull
    private int precio;
    @Column(unique = true)
    private String imagen;
    private String categoria;
    private String descripcion;
    @Positive(message = "Las unidades deben ser mayor a 0")
    private int unidades;

}
