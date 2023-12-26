package ru.practicum.shareit.validmark;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = CheckDate.class)
public @interface DateNotNull {
    String message() default "Date don't be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
