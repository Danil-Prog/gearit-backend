package com.gearit.api.config.filter;

import com.gearit.api.entity.enpoint.Endpoint;
import com.gearit.api.service.accesspolicy.AccessPolicyService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AccessPolicyAuthenticationFilter extends OncePerRequestFilter {

    private final AccessPolicyService accessPolicyService;

    private final Logger logger = LoggerFactory.getLogger(AccessPolicyAuthenticationFilter.class);

    @Autowired
    public AccessPolicyAuthenticationFilter(AccessPolicyService accessPolicyService) {
        this.accessPolicyService = accessPolicyService;
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
                .anyMatch(endpoint -> endpoint.getPath().equals(requestURI));

        if (!isPathAllowed) {
            logger.info(
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

    private static void asResponseForbidden(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Insufficient rights to access the resource\"}");
    }
}
