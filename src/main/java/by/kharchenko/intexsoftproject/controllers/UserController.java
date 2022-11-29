package by.kharchenko.intexsoftproject.controllers;


import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.UserDto;
import by.kharchenko.intexsoftproject.model.service.UserService;
import by.kharchenko.intexsoftproject.security.JwtAuthentication;
import by.kharchenko.intexsoftproject.util.validator.PhotoValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController extends AbstractController {

    private UserService userService;

    @GetMapping("")
    public ResponseEntity<UserDto> getUser() throws ServiceException {
        Long id = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        Optional<UserDto> optionalUserDto = userService.findById(id);
        return ResponseEntity.ok(optionalUserDto.get());
    }

    @PostMapping("/photo")
    public void photo(@RequestParam("file") MultipartFile file) throws ServiceException {
        Long id = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        userService.addPhoto(file, id);
    }

}
