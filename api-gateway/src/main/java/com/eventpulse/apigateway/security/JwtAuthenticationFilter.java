package com.eventpulse.apigateway.security;

import io.jsonwebtoken.Claims;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private static final String BEARER_PREFIX = "Bearer ";
    private static final String HEADER_USER = "X-User-Name";
    private static final String HEADER_ROLE = "X-User-Role";

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {


        ServerWebExchange cleaned = exchange.mutate()
                .request(r -> r.headers(h -> {
                    h.remove(HEADER_USER);
                    h.remove(HEADER_ROLE);
                }))
                .build();

        String authHeader = cleaned.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return chain.filter(cleaned);
        }

        String token = authHeader.substring(BEARER_PREFIX.length());

        try {
            Claims claims = jwtUtil.validateAndGetClaims(token);
            String username = claims.getSubject();
            String role = claims.get("role", String.class);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

            ServerWebExchange authenticated = cleaned.mutate()
                    .request(r -> r.headers(h -> {
                        h.add(HEADER_USER, username);
                        h.add(HEADER_ROLE, role);
                    }))
                    .build();

            return chain.filter(authenticated)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));

        } catch (Exception e) {

            return chain.filter(cleaned);
        }
    }
}