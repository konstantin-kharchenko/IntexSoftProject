package by.kharchenko.intexsoftproject.controllers;

import by.kharchenko.intexsoftproject.exception.ExistsException;
import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CustomTokenDto;
import by.kharchenko.intexsoftproject.model.dto.RegisterUserDto;
import by.kharchenko.intexsoftproject.model.dto.SignInUserDto;
import by.kharchenko.intexsoftproject.model.service.UserService;
import by.kharchenko.intexsoftproject.util.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class SignController extends AbstractController {

    private static final String REFRESH_TOKEN = "Refresh-Token";

    private final UserService userService;
    private final UserValidator userValidator;

    @PostMapping("/sign-in")
    public ResponseEntity<CustomTokenDto> signIn(@Valid @RequestBody SignInUserDto signInUserDto) throws ServiceException {
        Optional<CustomTokenDto> optionalCustomTokenDto = userService.signIn(signInUserDto);
        return ResponseEntity.ok(optionalCustomTokenDto.get());
    }

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult) throws ServiceException, ExistsException {
        userValidator.validate(registerUserDto, bindingResult);
        if (bindingResult.hasErrors()) {
            throw new ExistsException(bindingResult.getAllErrors());
        }
        userService.register(registerUserDto);
    }

    @GetMapping("/refresh")
    public ResponseEntity<CustomTokenDto> refresh(@RequestHeader(name = REFRESH_TOKEN) String refreshToken) throws ServiceException {
        Optional<CustomTokenDto> tokenDtoOptional = userService.refresh(refreshToken);
        return ResponseEntity.ok(tokenDtoOptional.get());

    }
}
