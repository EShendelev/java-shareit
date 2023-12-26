package ru.practicum.shareit.validmark;

import ru.practicum.shareit.booking.dto.BookItemRequestDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class CheckDate implements ConstraintValidator<DateNotNull, BookItemRequestDto> {
    @Override
    public void initialize(DateNotNull constraintAnnotation) {
    }

    @Override
    public boolean isValid(BookItemRequestDto value, ConstraintValidatorContext context) {
        LocalDateTime start = value.getStart();
        LocalDateTime end = value.getEnd();
        return start != null && end != null;
    }

}
