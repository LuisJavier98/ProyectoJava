package com.proyecto.Proyecto.Swagger.Schemas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActualizarContraseña {
    private String contraseña;
    private String contraseñaNueva;

}
