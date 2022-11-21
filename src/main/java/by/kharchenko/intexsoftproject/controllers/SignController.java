package by.kharchenko.intexsoftproject.controllers;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CustomTokenDto;
import by.kharchenko.intexsoftproject.model.dto.RegisterUserDto;
import by.kharchenko.intexsoftproject.model.dto.SignInUserDto;
import by.kharchenko.intexsoftproject.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class SignController {

    private static final String REFRESH_TOKEN = "Refresh-Token";

    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity signIn(@Valid @RequestBody SignInUserDto signInUserDto, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<CustomTokenDto> optionalCustomTokenDto = userService.signIn(signInUserDto);
            return ResponseEntity.ok(optionalCustomTokenDto.get());
        } catch (ServiceException | UsernameNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            userService.register(registerUserDto);
        } catch (ServiceException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity refresh(@RequestHeader(name = REFRESH_TOKEN) String refreshToken) {
        try {
            Optional<CustomTokenDto> tokenDtoOptional = userService.refresh(refreshToken);
            return ResponseEntity.ok(tokenDtoOptional.get());
        } catch (ServiceException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
