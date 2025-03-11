package com.iocode.web.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Component
public class JwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Value("${principal}")
    private String principal;

    @Value("${clientId}")
    private String clientId;

    private static final Logger log = LoggerFactory.getLogger(JwtConverter.class);

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
            Collection<GrantedAuthority> authorities = Stream.concat(
                    Optional.of(new JwtGrantedAuthoritiesConverter().convert(jwt))
                            .orElse(Set.of())
                            .stream(),
                    extractResourceRoles(jwt).stream()
            ).collect(Collectors.toSet());
            return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("resource_access");
        Map<String, Object> resource;
        Collection<String> resourceRoles;
        log.info("Extracting the resource roles from the token {}", resourceAccess);
        if (resourceAccess == null
                || (resource = (Map<String, Object>) resourceAccess.get(clientId)) == null
                || (resourceRoles = (Collection<String>) resource.get("roles")) == null) {
            return Set.of();
        }
        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}
