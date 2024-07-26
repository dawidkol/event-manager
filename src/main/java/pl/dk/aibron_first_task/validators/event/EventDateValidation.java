package pl.dk.aibron_first_task.validators.event;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import pl.dk.aibron_first_task.event.Event;
import pl.dk.aibron_first_task.event.dtos.SaveEventDto;


public class EventDateValidation implements ConstraintValidator<EventDate, Object> {

    @Override
    public void initialize(EventDate constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        String message = "Field eventEnd [%s] must be after eventStart [%s]";
        if (object instanceof Event) {
            boolean isAfter = (((Event) object).getEventEnd().isAfter((((Event) object).getEventStart())));
            String formatted = message.formatted(((Event) object).getEventEnd(), ((Event) object).getEventStart());
            context.buildConstraintViolationWithTemplate(formatted).addConstraintViolation();
            return isAfter;
        } else if (object instanceof SaveEventDto) {
            boolean isAfter = (((SaveEventDto) object).eventEnd().isAfter((((SaveEventDto) object).eventStart())));
            String formatted = message.formatted(((SaveEventDto) object).eventEnd(), ((SaveEventDto) object).eventStart());
            context.buildConstraintViolationWithTemplate(formatted).addConstraintViolation();
            context.buildConstraintViolationWithTemplate(("eventStart")).addPropertyNode(((SaveEventDto) object).eventStart().toString());
            context.buildConstraintViolationWithTemplate(("eventEnd")).addPropertyNode(((SaveEventDto) object).eventStart().toString());
            return isAfter;
        } else {
            return false;
        }
    }
}
