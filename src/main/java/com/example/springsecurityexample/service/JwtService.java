package com.example.springsecurityexample.service;

import com.example.springsecurityexample.dao.entity.Customer;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private String SECRET_KEY = String.valueOf(generateKey());

    //run eleyende error verib 256 bitlik key isteyirdi deye bele bir method
    public static String generateKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); //keyin olcusunu veririk
        SecretKey secretKey = keyGen.generateKey(); //keyin generate olunmasi
        //keyi Base64 formatina cevirir
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    } //daha sonra yuxarida methodu cagiririq string kimi secret keye menimsedilir

    private final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000; // 15 deqiqe
    private final long REFRESH_TOKEN_VALIDITY = 7 * 24 * 60 * 60 * 1000; // 7 gun

    public JwtService() throws Exception {
    }

    public String createAccessToken(Customer customer) {
        return Jwts.builder()
                .setSubject(customer.getEmail()) //JWT nin subjecti email olur belece tokenin hansi usere aid oldugu saxlanilir
                .setIssuedAt(new Date()) //tokenin yaradilma tarixi
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_VALIDITY)) //token aktiv olma vaxtini set eleyir
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) //JWT tokenin HS256 alqoritmi ile signatureni sifreleyir
                .compact();
    }

    //refresh tokenle eyni mentiq
    public String createRefreshToken(Customer customer) {
        return Jwts.builder()
                .setSubject(customer.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    //JWT tokenden customerin emailini cixarir
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY) //tokeni parse etmek ucun secret keyi menimsedir
                .parseClaimsJws(token) //JWT tokeni parse eleyir ve bodysini alir
                .getBody() //yeni claim eleyir
                .getSubject(); //bayaq emaili subject kimi set elemisdik onu qaytarir burda
    }

    //JWT tokenin kecerliliyini yoxlayir, yeni vaxti dolubmu ve ya duzgun usere aidmi
    public boolean validateToken(String token, Customer customer) {
        String email = extractEmail(token); //tokenin icinden emaili cixarir
        return email.equals(customer.getEmail()) && !isTokenExpired(token);
    } //daha sonra sistemdeki userin email adresi ile eynimi birde vaxti qurtaribmi yoxlayir

    //JWT tokenin vaxtinin qurtarib qurtarmadigini yoxlayir
    private boolean isTokenExpired(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()  //tokenin bitis vaxtini qaytarir
                .before(new Date()); //eger bitibse true qaytarir
    }
}