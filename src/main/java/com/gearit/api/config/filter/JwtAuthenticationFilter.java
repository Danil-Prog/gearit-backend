package com.gearit.api.config.filter;

import com.gearit.api.service.jwt.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.web.filter.*;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    public JwtAuthenticationFilter(
            JwtTokenProvider tokenProvider,
            UserDetailsService userDetailsService
    ) {
        this.tokenProvider = tokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = getTokenFromHeader(header);

        if (token != null && tokenProvider.validateToken(token)) {
            authenticateUserProvider(token);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateUserProvider(String token) {
        String username = tokenProvider.getUsernameFromToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        var auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String getTokenFromHeader(String header) {
        String token = null;

        if (header != null && !header.isBlank() && header.startsWith("Bearer ")) {
            token = header.substring(7);
        } else {
            logger.warn("Bearer token from header is empty.");
        }

        return token;
    }
}
