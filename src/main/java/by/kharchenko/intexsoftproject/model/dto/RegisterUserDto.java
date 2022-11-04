package by.kharchenko.intexsoftproject.model.dto;

import by.kharchenko.intexsoftproject.model.entity.RoleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserDto {

    @NotEmpty(message = "username must not be empty")
    @Pattern(regexp = "^[A-Za-z0-9-_]{3,}$", message = "Invalid")
    @JsonProperty("username")
    private String username;

    @NotEmpty(message = "password must not be empty")
    @Pattern(regexp = ".*[A-Za-z.-_*].*", message = "Invalid")
    @JsonProperty("password")
    private String password;

    @Email
    @NotEmpty(message = "email must not be empty")
    @JsonProperty("email")
    private String email;


    private Set<RoleType> roleTypes;
}
