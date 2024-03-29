package com.proyecto.Proyecto.Security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtUtil {

    private final String token_secret = System.getenv("JWT_TOKEN");

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(token_secret).parseClaimsJws(token).getBody();
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public String extractUserName(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public String extractUsuario_id(String token) {
        return extractClaims(token, Claims::getId);
    }

    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + 10 * 60 * 1000*999)).signWith(SignatureAlgorithm.HS256, token_secret).compact();
    }

    public Long getUserId(String token) {
        return extractAllClaims(token).get("id", Long.class);
    }

    public String generateToken(String username, Long id) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", id);
        return createToken(claims, username);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }
}
