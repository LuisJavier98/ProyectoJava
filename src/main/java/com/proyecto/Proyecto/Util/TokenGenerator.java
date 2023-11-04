package com.proyecto.Proyecto.Util;

import java.util.UUID;

public class TokenGenerator {
    public static String generateActivationToken() {
        return UUID.randomUUID().toString();
    }
}
