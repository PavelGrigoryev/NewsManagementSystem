package ru.clevertec.userservice.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.clevertec.userservice.service.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * The JwtServiceImpl implements JwtService interface for generating, validating, and extracting information from JSON Web Tokens.
 */
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private Long jwtExpiration;

    /**
     * Extracts the username from a JWT.
     *
     * @param token The JWT to extract the username from.
     * @return The username extracted from the JWT.
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a claim from a JWT using a provided function.
     *
     * @param token          The JWT to extract the claim from.
     * @param claimsResolver The function used to extract the claim.
     * @param <T>            The type of the extracted claim.
     * @return The extracted claim.
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT for a given user details object.
     *
     * @param userDetails The user details object to generate the JWT for.
     * @return The generated JWT.
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("roles", userDetails.getAuthorities());
        return generateToken(extraClaims, userDetails);
    }

    /**
     * Generates a JWT with additional claims for a given user details object.
     *
     * @param extraClaims The additional claims to include in the JWT.
     * @param userDetails The user details object to generate the JWT for.
     * @return The generated JWT.
     */
    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates a JWT for a given user details object.
     *
     * @param token       The JWT to validate.
     * @param userDetails The user details object to validate the JWT for.
     * @return True if the JWT is valid for the given user details object, false otherwise.
     */
    @Override
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Checks if a JWT has expired.
     *
     * @param token The JWT to check for expiration.
     * @return True if the JWT has expired, false otherwise.
     */
    @Override
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT.
     *
     * @param token The JWT to extract the expiration date from.
     * @return The expiration date of the JWT.
     */
    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts all claims from a JWT.
     *
     * @param token The JWT to extract the claims from.
     * @return The claims extracted from the JWT.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Retrieves the signing key used for generating and validating JWTs.
     *
     * @return The signing key.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
