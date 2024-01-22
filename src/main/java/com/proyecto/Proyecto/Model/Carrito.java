package com.proyecto.Proyecto.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "carrito")

public class Carrito  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    private Usuario usuario;
    @ManyToOne
    private Producto producto;
    @Column(nullable = false)
    @Positive(message = "La cantidad debe ser mayor a cero")
    private int cantidad;

    public Carrito(Usuario usuario, Producto producto, int cantidad) {
        this.usuario = usuario;
        this.producto = producto;
        this.cantidad = cantidad;
    }
}

