package com.tts.car_insurance.security;

import com.tts.car_insurance.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Service
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        log.trace("Entering JwtRequestFilter for request URI: {}", requestUri);

        String token = null;
        String userId = null;
        String userEmail = null;
        var authHeader = request.getHeader(AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            log.trace("Authorization header found. Extracted JWT token.");

            try {
                userEmail = jwtUtil.extractUserEmail(token);
                userId = jwtUtil.extractUserId(token);
                log.debug("Successfully extracted token details - UserEmail: {}, UserId: {}", userEmail, userId);
            } catch (Exception e) {
                log.warn("Failed to extract details from JWT token for URI: {}. Error: {}", requestUri, e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Something is wrong!");
                return;
            }
        } else {
            log.trace("No Bearer token found in Authorization header for URI: {}", requestUri);
        }

        if (userId != null && !jwtUtil.isTokenValid(token, userEmail)) {
            log.warn("Invalid or expired JWT token for user: {} on URI: {}", userEmail, requestUri);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Your Session is Expired.Please Login in again!");
            return;
        }

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            List<GrantedAuthority> authorities = jwtUtil.resolveRole(token);

            log.debug("Resolved authorities for user {}: {}", userEmail, authorities);

            UserPrincipal principal = new UserPrincipal(userId,userEmail);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principal, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("Successfully authenticated user: {} with authorities: {}", userEmail, authorities);
        }

        log.trace("Exiting JwtRequestFilter, continuing filter chain for URI: {}", requestUri);
        filterChain.doFilter(request, response);
    }
}