package com.gearit.api.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gearit.api.config.SecurityConfig;
import com.gearit.api.entity.endpoint.Endpoint;
import com.gearit.api.service.accesspolicy.AccessPolicyService;
import com.gearit.common.exception.WebClientException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

@Component
public class AccessPolicyAuthenticationFilter extends OncePerRequestFilter {

    private final ObjectMapper mapper;
    private final AccessPolicyService accessPolicyService;

    private final Logger logger = LoggerFactory.getLogger(AccessPolicyAuthenticationFilter.class);

    @Autowired
    public AccessPolicyAuthenticationFilter(AccessPolicyService accessPolicyService) {
        this.accessPolicyService = accessPolicyService;
        this.mapper = new ObjectMapper();
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            filterChain.doFilter(request, response);
            return;
        }

        String httpMethod = request.getMethod();
        String requestURI = request.getRequestURI();

        // Если текущий URL является публичным и разрешенным для всех - пропускаем.
        if (Arrays.asList(SecurityConfig.PERMIT_ALL_ENDPOINTS).contains(httpMethod)) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isPathAllowed;

        GrantedAuthority accessPolicy = authentication.getAuthorities().stream().findFirst().orElse(null);

        if (accessPolicy == null) {
            logger.info("Access policy authentication is null");
            asResponseForbidden(response);
            return;
        }

        Set<Endpoint> endpoints = accessPolicyService.getEndpointsByAccessPolicyName(accessPolicy.getAuthority());

        isPathAllowed = endpoints.stream()
                .filter(endpoint -> endpoint.getMethod().equals(httpMethod))
                .anyMatch(endpoint -> requestURI.matches(endpoint.getPath()));

        if (!isPathAllowed) {
            logger.warn(
                    "User with name: [{}] requested access to a protected resource: [{}: {}] and was denied",
                    authentication.getName(),
                    httpMethod,
                    requestURI
            );

            asResponseForbidden(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void asResponseForbidden(HttpServletResponse response) throws IOException {
        var exception = new WebClientException(
                "Failed to authentication request",
                "Insufficient rights to access the resource"
        );

        String error = mapper.writer().writeValueAsString(exception);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        response.setContentType("application/json");
        response.getWriter().write(error);
    }
}
