package com.proyecto.Proyecto.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Table(name = "compras")
public class Compras  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @Schema(description = "Usuario", example = "Libro")
    private Usuario usuario;
    @ManyToOne
    private Producto producto;
    @Positive(message = "La cantidad debe ser mayor a cero")
    private int cantidad;
    @NotBlank(message = "Por favor , indique una dirección")
    private String direccion;
    private Date fechaCompra;

}
