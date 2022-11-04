package by.kharchenko.intexsoftproject.controller;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CustomTokenDto;
import by.kharchenko.intexsoftproject.model.dto.RegisterUserDto;
import by.kharchenko.intexsoftproject.model.dto.SignInUserDto;
import by.kharchenko.intexsoftproject.model.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.validation.Valid;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SignController {

    private static final String REFRESH_TOKEN = "Refresh-Token";
    UserService userService;

    public SignController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-in")
    public ResponseEntity signIn(@Valid @RequestBody SignInUserDto signInUserDto, BindingResult bindingResult) throws ServletException {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            Optional<CustomTokenDto> optionalCustomTokenDto = userService.signIn(signInUserDto);
            if (optionalCustomTokenDto.isPresent()) {
                return ResponseEntity.ok(optionalCustomTokenDto.get());
            }
        } catch (ServiceException e) {
            throw new ServletException(e);
        }
        return ResponseEntity.status(400).build();
    }

    @PostMapping("/sign-up")
    public void signUp(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult) throws ServletException {
        if (bindingResult.hasErrors()) {
            throw new ServletException(bindingResult.toString());
        }
        try {
            boolean isAdd = userService.signUp(registerUserDto);
            if (!isAdd) {
                throw new ServletException("Failed to add user");
            }
        } catch (ServiceException e) {
            throw new ServletException(e);
        }
    }

    @GetMapping("/refresh")
    public ResponseEntity refresh(@RequestHeader(name = REFRESH_TOKEN) String refreshToken) throws ServletException {
        try {
            Optional<CustomTokenDto> tokenDtoOptional = userService.refresh(refreshToken);
            if (tokenDtoOptional.isPresent()) {
                return ResponseEntity.ok(tokenDtoOptional.get());
            }
            return ResponseEntity.status(401).build();
        } catch (ServiceException e) {
            throw new ServletException(e);
        }
    }
}
