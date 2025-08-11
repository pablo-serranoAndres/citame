package com.milibrodereservas.citame.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

// Esta clase se va a llamar para verificar el token pasado a la api

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtConfig jwtConfig;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        if ((authHeader == null) || !authHeader.startsWith("Bearer ")) {
            // sin autorizacion por token
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.jwtSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            final String userName = claims.getSubject();
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userName, null, List.of());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
