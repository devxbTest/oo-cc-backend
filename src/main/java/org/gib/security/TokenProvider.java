package org.gib.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.gib.model.UserEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TokenProvider {

    private static final String SECRET_KEY = "NMA8JPctFuna59f5";

    public String create(UserEntity entity) {
        Date expireDate = Date.from(
        Instant.now()
            .plus(1, ChronoUnit.DAYS));
        return Jwts.builder()
            .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
            .setSubject(entity.getId())
            .setIssuer("todo app")
            .setIssuedAt(new Date())
            .setExpiration(expireDate)
            .compact();
    }

    public String validateAndGetUserId(String token) {
        Claims claims = Jwts.parser()
            .setSigningKey(SECRET_KEY)
            .parseClaimsJws(token)
            .getBody();

        return claims.getSubject();
    }

}
