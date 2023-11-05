package com.proyecto.Proyecto.Swagger.Schemas;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Login {
    @Schema(description = "email")
    private String email;
    @Schema(description = "contraseña")
    private String contraseña;

}
