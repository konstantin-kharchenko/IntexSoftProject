package by.kharchenko.intexsoftproject.model.service;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CardNumberDto;
import by.kharchenko.intexsoftproject.model.dto.GenerateCardUserDto;

public interface CardService {
    void generateNumber(GenerateCardUserDto generateCardUserDto) throws ServiceException;

    void addCard(CardNumberDto cardNumberDto) throws ServiceException;
}
