package com.proyecto.Proyecto.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "compras")
public class Compras {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @Schema(description = "Usuario", example = "Libro")
    private Usuario usuario;
    @ManyToOne
    private Producto producto;
    private int cantidad;
    private String direccion;
    private Date fechaCompra;

}
