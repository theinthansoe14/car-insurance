package com.tts.car_insurance.utils;


import com.tts.car_insurance.enums.ROLE;
import com.tts.car_insurance.model.User;
import com.tts.car_insurance.security.JwtProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;


@Service
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtProperty jwtProperty;
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);


    public String generateToken( User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtProperty.getExpirationTime());

        logger.info("SECURITY [INFO]: JWT Token generated successfully via method: generateToken");

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .claim("userId", user.getId())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String token,String name){
        String userName = extractUserEmail(token);
        return userName.equalsIgnoreCase(name) && !isTokenExpired(token);
    }
    public List<GrantedAuthority> resolveRole(String token){
        Claims claims = extractAllClaims(token);
        String role =(String) claims.get("role");
        return AuthorityUtils.createAuthorityList("ROLE_"+ ROLE.resolve(role));
    }


    public boolean isTokenExpired(String token) {

        return extractClaim(token,Claims::getExpiration).before(new Date());
    }

    public String extractUserEmail(String token) {

        return extractClaim(token, Claims::getSubject);
    }

    public String extractUserId(String token){
        Claims claims = extractAllClaims(token);
        return claims.get("userId").toString();
    }

    private <T>  T extractClaim(String token, Function<Claims , T> claimResolver) {
        Claims claim = extractAllClaims(token);
        return claimResolver.apply(claim);

    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }


    private SecretKey getKey() {
        byte[] decode = Base64.getDecoder().decode(jwtProperty.getSecretKey());
        return Keys.hmacShaKeyFor(decode);

    }
}
