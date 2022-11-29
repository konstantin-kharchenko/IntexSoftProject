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
public class LoyaltyCardController extends AbstractController{
    private final CardService cardService;

    @PostMapping("/generate-card-number")
    public void generateCardNumber(@Valid @RequestBody GenerateCardUserDto generateCardUserDto) throws ServiceException {
        cardService.generateNumber(generateCardUserDto);
    }

    @PostMapping("/create-card")
    public void createCard(@Valid @RequestBody CardNumberDto cardNumberDto) throws ServiceException {
        cardService.addCard(cardNumberDto);
    }
}
