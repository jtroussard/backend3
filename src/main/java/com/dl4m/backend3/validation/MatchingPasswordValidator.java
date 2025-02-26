package com.dl4m.backend3.validation;

import com.dl4m.backend3.dto.request.RegistrationRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class MatchingPasswordValidator implements ConstraintValidator<MatchingPassword, RegistrationRequest> {

    @Override
    public void initialize(MatchingPassword constraintAnnotation) {
        // No initialization required
    }

    @Override
    public boolean isValid(RegistrationRequest request, ConstraintValidatorContext context) {
        if (request.getPassword() == null || request.getConfirmPassword() == null) {
            return false;
        }
        return request.getPassword().equals(request.getConfirmPassword());
    }
}
