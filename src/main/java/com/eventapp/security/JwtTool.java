package com.eventapp.security;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTool {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.duration}")
    private long duration;

    public String createToken(String user){
        return Jwts.builder().issuedAt(new Date(System.currentTimeMillis())).
                expiration(new Date(System.currentTimeMillis()+duration)).
                subject(String.valueOf(user.get())).
                signWith(Keys.hmacShaKeyFor(secret.getBytes())).
                compact();
    }

    public void verifyToken(String token){
        Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).
                build().parse(token);
    }

    public int getUsername(String token){
        return Integer.parseInt(Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).
                build().parseSignedClaims(token).getPayload().getSubject());
    }