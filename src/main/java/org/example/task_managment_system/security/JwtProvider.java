package org.example.task_managment_system.security;

import io.jsonwebtoken.*;
import org.example.task_managment_system.exception.JwtException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secretkey}")
    private String key;
    @Value("${jwt.token.ttl}")
    private Long ttl;


    public String generateToken(String phoneNumber) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(phoneNumber)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttl))
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }


    public String getPhoneNumberFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }


    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            throw new JwtException("JWT token has expired: " + e.getMessage());
        } catch (SignatureException e) {
            throw new JwtException("Invalid JWT signature: " + e.getMessage());
        } catch (Exception e) {
            throw new JwtException("Invalid JWT token: " + e.getMessage());
        }
    }
}