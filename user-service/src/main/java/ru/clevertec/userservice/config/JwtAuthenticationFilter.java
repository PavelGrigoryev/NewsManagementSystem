package ru.clevertec.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.clevertec.exceptionhandlerstarter.exception.NoSuchUserEmailException;
import ru.clevertec.exceptionhandlerstarter.model.IncorrectData;
import ru.clevertec.userservice.service.JwtService;

import java.io.IOException;

/**
 * JwtAuthenticationFilter class extends OncePerRequestFilter and provides implementation for doFilterInternal.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    /**
     * Filters incoming requests for JWT authentication.
     *
     * @param request     HttpServletRequest.
     * @param response    HttpServletResponse.
     * @param filterChain FilterChain.
     * @throws ServletException if a servlet error occurs.
     * @throws IOException      if an I/O error occurs.
     */
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            String jwt;
            String userEmail;
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
                if (Boolean.TRUE.equals(jwtService.isTokenValid(jwt, userDetails))) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (JwtException | AuthenticationException | NoSuchUserEmailException e) {
            handleException(response, e);
        }
    }

    /**
     * Handles exceptions related to JWT authentication and sends an unauthorized response.
     *
     * @param response  HttpServletResponse
     * @param exception Exception
     * @throws IOException if an I/O error occurs
     */
    public void handleException(HttpServletResponse response, Exception exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("content-type", MediaType.APPLICATION_JSON.toString());
        response.setCharacterEncoding("utf-8");
        IncorrectData incorrectData = new IncorrectData(
                exception.getClass().getSimpleName(),
                exception.getMessage(),
                HttpStatus.UNAUTHORIZED.toString()
        );
        String responseMessage = objectMapper.writeValueAsString(incorrectData);
        log.error(incorrectData.toString());
        response.getWriter().write(responseMessage);
    }

}
