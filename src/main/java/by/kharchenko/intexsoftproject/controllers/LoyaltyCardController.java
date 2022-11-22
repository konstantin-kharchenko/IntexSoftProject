package by.kharchenko.intexsoftproject.controllers;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CardNumberDto;
import by.kharchenko.intexsoftproject.model.dto.GenerateCardUserDto;
import by.kharchenko.intexsoftproject.model.service.CardService;
import by.kharchenko.intexsoftproject.model.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
public class LoyaltyCardController {
    private final CardService cardService;

    @PostMapping("/generate-card-number")
    public ResponseEntity generateCardNumber(@Valid @RequestBody GenerateCardUserDto generateCardUserDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            cardService.generateNumber(generateCardUserDto);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create-card")
    public ResponseEntity createCard(@Valid @RequestBody CardNumberDto cardNumberDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getAllErrors(), HttpStatus.BAD_REQUEST);
        }
        try {
            cardService.addCard(cardNumberDto);
        } catch (ServiceException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok().build();
    }
}
