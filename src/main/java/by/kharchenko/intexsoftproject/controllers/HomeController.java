package by.kharchenko.intexsoftproject.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity home(){
        return ResponseEntity.ok("HOME PAGE");
    }
}
