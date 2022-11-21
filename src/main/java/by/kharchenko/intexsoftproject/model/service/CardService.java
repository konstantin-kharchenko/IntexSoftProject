package by.kharchenko.intexsoftproject.model.service;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CardNumberDto;

public interface CardService {
    void addCard(CardNumberDto cardNumberDto) throws ServiceException;
}
