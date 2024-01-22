package com.proyecto.Proyecto.Responses;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Configuration
public class Validations {

    String email;
    public static String NUMBER_REGEX = "^(\\+51|0051|51)?[9]\\d{8}$";

    public Boolean isValidEmail() {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(this.email);
        return matcher.matches();

    }
}
