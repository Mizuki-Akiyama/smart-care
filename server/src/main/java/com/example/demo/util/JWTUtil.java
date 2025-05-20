package com.example.demo.util;

import com.example.demo.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.UUID;

public class JWTUtil {
    private static final String key = "This-is-my-secret-key";

    public static String createToken(String userId, String password){

        JwtBuilder jwtBuilder = Jwts.builder();
        String token = jwtBuilder
                .setHeaderParam("typ","JWT")
                .setHeaderParam("alg","HS256")
                .claim("password",password)
                .claim("userId",userId)
                .setSubject("user")
                .setExpiration(new Date(System.currentTimeMillis()+1000*60*60*24))
                .signWith(SignatureAlgorithm.HS256,key)
                .setId(UUID.randomUUID().toString())
                .compact();

        return token;
    }

    public static boolean verifyToken(String token) {
        try {

            Claims jws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static AuthUser getUser(String token) {
        try {
            Claims jws = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();

            String userId = jws.get("userId").toString();
            String password = jws.get("password").toString();

            return new AuthUser(userId, password,token);

        } catch (Exception e) {
            return null;
        }
    }
}
