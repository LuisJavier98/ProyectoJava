package com.proyecto.Proyecto.Security.jwt;

import com.proyecto.Proyecto.Security.CustomDetailsServices;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    Claims claims = null;
    private String username = null;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private CustomDetailsServices customDetailsServices;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getServletPath().matches("/api/usuario/activarCuenta|/api/usuario/login|/api/productos|/api/usuario|/api/productos/[0-9]+|/api/usuario/enviarCorreo|/images/[a-zA-Z0-9%.-]+")) {
            System.out.println("entra aqui");
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader("Authorization");
            String token = null;
            if (authorizationHeader == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                String jsonResponse = "{\"message\": \"Es necesario un Bearer token de autorización\"}";
                response.getWriter().write(jsonResponse);
                return;
            }
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                token = authorizationHeader.substring(7);
                try {
                    claims = jwtUtil.extractAllClaims(token);
                    username = jwtUtil.extractUserName(token);
                } catch (ExpiredJwtException ex) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    String jsonResponse = "{\"message\": \"El token es invalido o ha expirado\"}";
                    response.getWriter().write(jsonResponse);
                    return;
                }
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = customDetailsServices.loadUserByUsername(username);
                if (jwtUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    new WebAuthenticationDetailsSource().buildDetails(request);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }
}
