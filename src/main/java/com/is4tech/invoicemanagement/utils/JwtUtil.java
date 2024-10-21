package com.is4tech.invoicemanagement.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil {
    public static String extractUserIdFromToken(String token) {
        String secretKey = "f26d876d63f21f082d4e58bc1f2b4552b22711c1832318fbf49367e87cfd6ca7";

        Claims claims = Jwts.parser()
                .setSigningKey(secretKey.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject(); // Asumiendo que el userId está en el campo 'subject' del token
    }
}
