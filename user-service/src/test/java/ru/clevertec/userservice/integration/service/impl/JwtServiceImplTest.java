package ru.clevertec.userservice.integration.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.userservice.integration.BaseIntegrationTest;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.service.JwtService;
import ru.clevertec.userservice.util.json.TokenTxtSupplier;
import ru.clevertec.userservice.util.testbuilder.UserTestBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@RequiredArgsConstructor
class JwtServiceImplTest extends BaseIntegrationTest {

    private final JwtService jwtService;

    @Test
    @DisplayName("test extractUsername should return expected email")
    void testExtractUsernameShouldRerunExpectedEmail() throws IOException {
        User user = UserTestBuilder.aUser().build();
        String token = TokenTxtSupplier.getToken2048Request();

        String email = jwtService.extractUsername(token);

        assertThat(email).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("test extractClaim should return expected email")
    void testExtractClaimShouldRerunExpectedEmail() throws IOException {
        User user = UserTestBuilder.aUser().build();
        String token = TokenTxtSupplier.getToken2048Request();

        String email = jwtService.extractClaim(token, Claims::getSubject);

        assertThat(email).isEqualTo(user.getEmail());
    }

    @Test
    @DisplayName("test extractClaim should return expected token expiration")
    void testExtractClaimShouldRerunExpectedTokenExpiration() throws IOException {
        Calendar calendar = new GregorianCalendar(2048, Calendar.OCTOBER, 17, 23, 52, 36);
        Date expectedDate = calendar.getTime();
        String token = TokenTxtSupplier.getToken2048Request();

        Date actualDate = jwtService.extractClaim(token, Claims::getExpiration);

        assertThat(actualDate).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("test extractClaim should return expected roles")
    void testExtractClaimShouldRerunExpectedRoles() throws IOException {
        String expectedRoles = "[{authority=ADMIN}]";
        String token = TokenTxtSupplier.getToken2048Request();

        String actualRoles = jwtService.extractClaim(token, claims -> claims.get("roles")).toString();

        assertThat(actualRoles).isEqualTo(expectedRoles);
    }

    @Test
    @DisplayName("test generateToken should return token with expected length")
    void testGenerateTokenShouldRerunTokenWithExpectedLength() {
        User user = UserTestBuilder.aUser().build();
        int expectedLength = 192;

        String token = jwtService.generateToken(user);

        assertThat(token).hasSize(expectedLength);
    }

    @Test
    @DisplayName("test generateToken should return token with expiration date plus one day")
    void testGenerateTokenShouldRerunTokenWithExpirationDatePlusOneDay() {
        User user = UserTestBuilder.aUser().build();
        LocalDate expectedDate = LocalDate.now().plusDays(1);

        String token = jwtService.generateToken(user);
        LocalDate actualDate = jwtService.extractExpiration(token)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        assertThat(actualDate).isEqualTo(expectedDate);
    }

    @Test
    @DisplayName("test generateToken should put extra claims in token")
    void testGenerateTokenShouldPutExtraClaimsInToken() {
        User user = UserTestBuilder.aUser().build();
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("name", user.getFirstname());

        String token = jwtService.generateToken(extraClaims, user);
        String firstName = jwtService.extractClaim(token, claims -> claims.get("name")).toString();

        assertThat(firstName).isEqualTo(user.getFirstname());
    }

    @Test
    @DisplayName("test isTokenValid should return true")
    void testIsTokenValidShouldRerunTrue() throws IOException {
        User user = UserTestBuilder.aUser().build();
        String token = TokenTxtSupplier.getToken2048Request();

        Boolean actual = jwtService.isTokenValid(token, user);

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("test isTokenValid should throw ExpiredJwtException")
    void testIsTokenValidShouldThrowExpiredJwtException() throws IOException {
        User user = UserTestBuilder.aUser().build();
        String token = TokenTxtSupplier.getExpiredTokenRequest();

        assertThrows(ExpiredJwtException.class, () -> jwtService.isTokenValid(token, user));
    }

    @Test
    @DisplayName("test isTokenValid should throw MalformedJwtException")
    void testIsTokenValidShouldThrowMalformedJwtException() {
        User user = UserTestBuilder.aUser().build();
        String token = "JWT";

        assertThrows(MalformedJwtException.class, () -> jwtService.isTokenValid(token, user));
    }

    @Test
    @DisplayName("test isTokenValid should throw SignatureException")
    void testIsTokenValidShouldThrowSignatureException() throws IOException {
        User user = UserTestBuilder.aUser().build();
        String token = TokenTxtSupplier.getBadSignatureTokenRequest();

        assertThrows(SignatureException.class, () -> jwtService.isTokenValid(token, user));
    }

    @Test
    @DisplayName("test isTokenExpired should return true")
    void testIsTokenExpiredShouldRerunTrue() throws IOException {
        User user = UserTestBuilder.aUser().build();
        String token = TokenTxtSupplier.getToken2048Request();

        Boolean actual = jwtService.isTokenValid(token, user);

        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("test extractExpiration return should expected Date")
    void testExtractExpirationShouldRerunExpectedDate() throws IOException {
        Calendar calendar = new GregorianCalendar(2048, Calendar.OCTOBER, 17, 23, 52, 36);
        Date expectedDate = calendar.getTime();
        String token = TokenTxtSupplier.getToken2048Request();

        Date actualDate = jwtService.extractExpiration(token);

        assertThat(actualDate).isEqualTo(expectedDate);
    }

}
