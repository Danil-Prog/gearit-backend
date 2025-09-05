package com.gearit.api.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gearit.common.exception.WebClientException;
import com.gearit.api.service.jwt.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final ObjectMapper mapper;
    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;


    @Autowired
    public JwtAuthenticationFilter(
            JwtTokenProvider tokenProvider,
            UserDetailsService userDetailsService
    ) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = getTokenFromHeader(header);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        if (tokenProvider.validateToken(token)) {
            authenticateUserProvider(token, request);
        } else {
            asResponseUnauthorized(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUserProvider(String token, HttpServletRequest request) {
        String username = tokenProvider.getEmailFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String getTokenFromHeader(String header) {
        String token = null;

        if (header != null && !header.isBlank() && header.startsWith(BEARER_PREFIX)) {
            token = header.substring(BEARER_PREFIX.length());
        }

        return token;
    }

    private void asResponseUnauthorized(HttpServletResponse response) throws IOException {
        var exception = new WebClientException("Failed to authentication request", "Invalid or expired token");
        String error = mapper.writer().writeValueAsString(exception);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(error);
    }
}
