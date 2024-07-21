package com.group6.swp391.jwt;

import com.group6.swp391.logout.ListToken;
import com.group6.swp391.config.CustomUserDetail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Service
public class JWTToken {

    @Autowired private ListToken listToken;

    @Value("${jwt.expiration}")
    private int JWT_EXPIRATION;

    @Value("${jwt.secret}")
    private String sceretString;

    @Value("${jwt.algorithms}")
    private String algorithm;  // 10 ngay

    private SecretKey SCRET_KEY;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Base64.getDecoder().decode(sceretString.getBytes(StandardCharsets.UTF_8));
        this.SCRET_KEY = new SecretKeySpec(keyBytes, algorithm);
    }

    public String generatedToken(CustomUserDetail customUserDetail) {
        Date date = new Date(System.currentTimeMillis());

        Date exp = new Date(System.currentTimeMillis() + JWT_EXPIRATION);

        return Jwts.builder()
                .subject(customUserDetail.getUsername())
                .issuedAt(date)
                .expiration(exp)
                .claim("userID", customUserDetail.getUserID())
                .claim("firstName", customUserDetail.getFirstName())
                .claim("lastName", customUserDetail.getLastName())
                .claim("role", customUserDetail.getGrantedAuthorities())
                .signWith(SCRET_KEY)
                .compact();
    }

    public String getEmailFromJwt(String token) {
        return getClaims(token, Claims::getSubject);
    }

    private <T> T getClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(
                Jwts.parser().verifyWith(SCRET_KEY).build().parseSignedClaims(token).getPayload());
    }
    public boolean validate(String token) {
        if(getEmailFromJwt(token) != null && !isExpired(token) && !listToken.isListToken(token)) {
            return true;
        }
        return false;
    }

    public boolean isExpired(String token) {
        return getClaims(token, Claims::getExpiration).before(new Date(System.currentTimeMillis()));
    }

}
