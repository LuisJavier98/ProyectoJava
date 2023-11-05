package com.proyecto.Proyecto.Swagger.Schemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NuevoUsuario {
    private String nombre;
    private String apellido;
    private String contraseña;
    private String email;
    private Long numero;
}
