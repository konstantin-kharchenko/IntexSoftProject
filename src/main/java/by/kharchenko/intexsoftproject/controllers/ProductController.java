package by.kharchenko.intexsoftproject.controllers;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.ProductPageDto;
import by.kharchenko.intexsoftproject.model.dto.SignInUserDto;
import by.kharchenko.intexsoftproject.model.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProductController {
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity products(@RequestBody ProductPageDto productPageDto){
        try {
            return ResponseEntity.ok(productService.findByCurrentPage(productPageDto));
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
