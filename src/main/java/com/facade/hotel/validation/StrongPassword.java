package com.facade.hotel.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Requires at least 8 characters with one uppercase letter, one lowercase letter
 * and one digit.
 */
@Documented
@Constraint(validatedBy = StrongPasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StrongPassword {

    String message() default "La contrasena debe tener al menos 8 caracteres, una mayuscula, una minuscula y un numero";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
