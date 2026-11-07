package com.qq.anno;

import com.qq.validation.StateValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {StateValidation.class})
@Target(FIELD)
@Retention(RUNTIME)
public @interface State
{
    String message() default "state校验的数只能是已发布或者草稿";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
