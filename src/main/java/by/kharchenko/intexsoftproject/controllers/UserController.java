package by.kharchenko.intexsoftproject.controllers;


import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.UserDto;
import by.kharchenko.intexsoftproject.model.service.UserService;
import by.kharchenko.intexsoftproject.security.JwtAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping("/user")
    public ResponseEntity getUser() throws ServiceException {
        Long id = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        Optional<UserDto> optionalUserDto = userService.findById(id);
        return ResponseEntity.ok(optionalUserDto.get());
    }

    @PostMapping("/photo")
    public ResponseEntity photo(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new ResponseEntity<>("File is empty", HttpStatus.BAD_REQUEST);
            }
            Long id = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
            userService.addPhoto(file, id);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

}
