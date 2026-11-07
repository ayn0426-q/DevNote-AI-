package com.qq;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class JWTtest {
    @Test
    public void testGen(){
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",1);
        claims.put("username","张三");
        String Token= JWT.create()
                  .withClaim("id",1)
                  .withClaim("username","张三")
                  .withExpiresAt(new Date(System.currentTimeMillis()+1000*60*60*12))
                  .sign(Algorithm.HMAC256("qq"));
           System.out.println(Token);
    }

    @Test
    public void testparese(){
        String Token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9." +
                "eyJpZCI6MSwidXNlcm5hbWUiOiLlvKDkuIkiLCJleHAiOjE3OTE5MjQ4NjJ9." +
                "X08f5QKgBowXLABo1ztmoRe1RKDaLyxdWrrwhLsHERk";

        JWTVerifier jwtVerifier=JWT.require(Algorithm.HMAC256("qq")).build();
        DecodedJWT decodedJWT=jwtVerifier.verify(Token);
        Map<String, Claim> claims=decodedJWT.getClaims();
        System.out.println(claims.get("id"));
        System.out.println(claims.get("username"));

    }

}
