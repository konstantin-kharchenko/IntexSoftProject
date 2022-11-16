package by.kharchenko.intexsoftproject.controllers;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CustomTokenDto;
import by.kharchenko.intexsoftproject.model.dto.RegisterUserDto;
import by.kharchenko.intexsoftproject.model.dto.SignInUserDto;
import by.kharchenko.intexsoftproject.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class SignController {

    private static final String REFRESH_TOKEN = "Refresh-Token";
    UserService userService;

    public SignController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity signIn(@Valid @RequestBody SignInUserDto signInUserDto, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        Optional<CustomTokenDto> optionalCustomTokenDto = userService.signIn(signInUserDto);
        return ResponseEntity.ok(optionalCustomTokenDto.get());
    }

    @PostMapping("/sign-up")
    public ResponseEntity signUp(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult) throws ServiceException {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        userService.signUp(registerUserDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/refresh")
    public ResponseEntity refresh(@RequestHeader(name = REFRESH_TOKEN) String refreshToken) throws ServiceException {
        Optional<CustomTokenDto> tokenDtoOptional = userService.refresh(refreshToken);
        return ResponseEntity.ok(tokenDtoOptional.get());
    }
}
