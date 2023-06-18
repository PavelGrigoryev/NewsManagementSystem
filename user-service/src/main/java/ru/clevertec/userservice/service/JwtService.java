package ru.clevertec.userservice.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * The JwtService interface provides generating, validating, and extracting information from JSON Web Tokens .
 */
public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(UserDetails userDetails);

    String generateToken(Map<String, Object> extraClaims, UserDetails userDetails);

    Boolean isTokenValid(String token, UserDetails userDetails);

    Boolean isTokenExpired(String token);

    Date extractExpiration(String token);

}
