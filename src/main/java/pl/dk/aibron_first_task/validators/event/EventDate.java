package pl.dk.aibron_first_task.validators.event;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Constraint(validatedBy = EventDateValidation.class)
@Target({TYPE_USE, PARAMETER})
@Retention(RUNTIME)
public @interface EventDate {
    String message() default "Invalid start or end date";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
