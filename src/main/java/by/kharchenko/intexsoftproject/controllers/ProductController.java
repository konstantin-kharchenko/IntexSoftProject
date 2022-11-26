package by.kharchenko.intexsoftproject.controllers;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.ProductDto;
import by.kharchenko.intexsoftproject.model.dto.PageDto;
import by.kharchenko.intexsoftproject.model.service.ProductService;
import by.kharchenko.intexsoftproject.model.service.UserService;
import by.kharchenko.intexsoftproject.security.JwtAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private ProductService productService;
    private UserService userService;

    @GetMapping("/products")
    public ResponseEntity products(@RequestBody PageDto pageDto, @RequestParam(value = "search") String search){
        try {
            return ResponseEntity.ok(productService.findByCurrentPage(pageDto, search));
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity product(@PathVariable("id") Long id){
        try {
            ProductDto productDto = productService.findById(id);
            return ResponseEntity.ok(productDto);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/picture/{id}")
    public ResponseEntity productPicture(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(productService.getPictureById(id));
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/instruction/{id}")
    public ResponseEntity productInstruction(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(productService.getInstructionById(id));
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/add-to-basket")
    public ResponseEntity addToBasket(@PathVariable("id") Long id){
        try {
            Long userId = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
            userService.addToBasket(id, userId);
            return ResponseEntity.ok().build();
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
