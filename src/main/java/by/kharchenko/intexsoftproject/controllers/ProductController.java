package by.kharchenko.intexsoftproject.controllers;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.PageDto;
import by.kharchenko.intexsoftproject.model.dto.PaginationDataDto;
import by.kharchenko.intexsoftproject.model.dto.ProductDto;
import by.kharchenko.intexsoftproject.model.service.ProductService;
import by.kharchenko.intexsoftproject.model.service.UserService;
import by.kharchenko.intexsoftproject.security.JwtAuthentication;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/product")
public class ProductController extends AbstractController {
    private ProductService productService;
    private UserService userService;

    @GetMapping("/products")
    public ResponseEntity<PaginationDataDto> products(@RequestBody PageDto pageDto, @RequestParam(value = "search") String search) throws ServiceException {
        return ResponseEntity.ok(productService.findByCurrentPage(pageDto, search));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> product(@PathVariable("id") Long id) throws ServiceException {
        return ResponseEntity.ok(productService.findById(id));
    }

    @GetMapping("/picture/{id}")
    public ResponseEntity<Resource> productPicture(@PathVariable("id") Long id) throws ServiceException {
        return ResponseEntity.ok(productService.getPictureById(id));
    }

    @GetMapping("/instruction/{id}")
    public ResponseEntity<Resource> productInstruction(@PathVariable("id") Long id) throws ServiceException {
        return ResponseEntity.ok(productService.getInstructionById(id));

    }

    @PostMapping("/{id}/add-to-basket")
    public void addToBasket(@PathVariable("id") Long id) throws ServiceException {
        Long userId = ((JwtAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
        userService.addToBasket(id, userId);
    }
}
