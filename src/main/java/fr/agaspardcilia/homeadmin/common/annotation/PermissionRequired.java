package fr.agaspardcilia.homeadmin.common.annotation;

import fr.agaspardcilia.homeadmin.security.Permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * To be used on controller methods. Will indicate that a given permission is required to perform the annotated operation.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionRequired {
    /**
     * @return the permission required to perform the operation.
     */
    Permission value();
}
