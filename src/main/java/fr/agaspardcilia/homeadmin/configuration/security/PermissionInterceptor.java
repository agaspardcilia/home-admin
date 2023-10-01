package fr.agaspardcilia.homeadmin.configuration.security;

import fr.agaspardcilia.homeadmin.common.annotation.PermissionRequired;
import fr.agaspardcilia.homeadmin.common.annotation.PermitAll;
import fr.agaspardcilia.homeadmin.common.exception.ApiForbiddenException;
import fr.agaspardcilia.homeadmin.common.exception.ApiInternalServerErrorException;
import fr.agaspardcilia.homeadmin.security.Authority;
import fr.agaspardcilia.homeadmin.security.Permission;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * An interceptor making sure the connected user has the right permissions to access a given endpoint.
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return HandlerInterceptor.super.preHandle(request, response, handler);
        }

        HandlerMethod methodHandler = (HandlerMethod) handler;
        boolean hasPermitAll = methodHandler.hasMethodAnnotation(PermitAll.class);
        boolean hasPermissionRequired = methodHandler.hasMethodAnnotation(PermissionRequired.class);

        if (!(hasPermitAll || hasPermissionRequired)) {
            throw new ApiInternalServerErrorException("Invalid endpoint configuration : '%s' has no configured Permission!".formatted(request.getRequestURI()));
        }
        if (hasPermissionRequired && hasPermitAll) {
            throw new ApiInternalServerErrorException("Invalid endpoint configuration : '%s' requires permission and no permission!".formatted(request.getRequestURI()));
        }

        if (hasPermissionRequired) {
            Permission methodPermission = Optional.ofNullable(methodHandler.getMethodAnnotation(PermissionRequired.class))
                    .map(PermissionRequired::value)
                    .orElseThrow();// No permission attached to the endpoint.
            getCurrentUserAuthorities().stream()
                    .map(Authority::getPermissions)
                    .flatMap(Collection::stream)
                    .filter(e -> e == methodPermission)
                    .findAny()
                    .orElseThrow(() -> new ApiForbiddenException("Current user is not authorized to perform this operation"));
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private Set<Authority> getCurrentUserAuthorities() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return Set.of();
        }

        return context.getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(Authority::valueOf).collect(Collectors.toUnmodifiableSet());
    }
}
