package com.alejandro.OpenEarth.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    Class<? extends Enum<?>> enumClass();
    String message() default "Invalid value. Must be one of the predifined enum values.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
