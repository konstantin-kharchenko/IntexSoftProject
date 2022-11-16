package by.kharchenko.intexsoftproject.controllers;


import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.UserDto;
import by.kharchenko.intexsoftproject.model.service.UserService;
import by.kharchenko.intexsoftproject.security.JwtAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity getUser() throws ServiceException {
        Long id = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        Optional<UserDto> optionalUserDto = userService.getUserById(id);
        return ResponseEntity.ok(optionalUserDto.get());
    }

}
