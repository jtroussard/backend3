package com.dl4m.backend3.dto.request;

import com.dl4m.backend3.validation.MatchingPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@MatchingPassword
public class RegistrationRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 4, max = 24, message = "Username must be between 4 and 24 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%]).{8,24}$",
            message = "Password must contain at least one uppercase, one lowercase, one number, and one special character (!@#$%)")
    private String password;

    @NotBlank(message = "Password confirmation is required")
    private String confirmPassword;
}
