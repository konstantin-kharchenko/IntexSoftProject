package by.kharchenko.intexsoftproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInUserDto {

    @NotEmpty(message = "username must not be empty")
    @Pattern(regexp = "^[^!#$%^&*()_\\s]*$", message = "Invalid")
    private String data;

    @NotEmpty(message = "password must not be empty")
    @Pattern(regexp = ".*[A-Za-z.-_*].*", message = "Invalid")
    private String password;
}
