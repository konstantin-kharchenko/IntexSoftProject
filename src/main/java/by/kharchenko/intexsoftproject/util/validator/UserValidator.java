package by.kharchenko.intexsoftproject.util.validator;


import by.kharchenko.intexsoftproject.model.dto.RegisterUserDto;
import by.kharchenko.intexsoftproject.model.entity.User;
import by.kharchenko.intexsoftproject.model.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.query.JpaQueryCreator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {

    private UserRepository userRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterUserDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterUserDto user = (RegisterUserDto) target;
        Optional<User> optionalUser = userRepository.findByEmailOrUsername(user.getEmail(), user.getUsername());
        if (optionalUser.isPresent()) {
            User bdUser = optionalUser.get();
            if (bdUser.getUsername().equals(user.getUsername())) {
                errors.rejectValue("username", "", "Username is already exists");
            }
            if (bdUser.getEmail().equals(user.getEmail())) {
                errors.rejectValue("email", "", "Email is already exists");
            }
        }
    }
}
