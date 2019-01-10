package ${basePackage}.ws.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.LinkedList;
import java.util.List;

/**
 * Validation error details
 */
public class ValidationErrors {

    @JsonProperty("errors")
    private final List<ValidationError> fieldErrors = new LinkedList<>();

    public ValidationErrors(ConstraintViolationException ex) {
        for (ConstraintViolation error : ex.getConstraintViolations()) {
            fieldErrors.add(new ValidationError(error));
        }
    }

    public ValidationErrors(BindingResult bindingResult) {
        for (ObjectError error : bindingResult.getAllErrors()) {
            fieldErrors.add(new ValidationError(error));
        }
    }

    public List<ValidationError> getFieldErrors() {
        return fieldErrors;
    }
}
