package com.proyecto.Proyecto.Responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Response  {
    Object message;
    Object body;
    String bodyName;
    Boolean isBadResponse;

    public Response(Object message, Boolean isBadResponse) {
        this.message = message;
        this.isBadResponse = isBadResponse;
    }

    public HashMap<String ,Object> mensajeResponse(){
        HashMap<String,Object> respuesta=new HashMap<>();
        respuesta.put("mensaje",this.message);
        if(!Objects.isNull(this.body)){
        respuesta.put(String.format("%s",this.bodyName),this.body);
        }
        return respuesta;
    }
}
