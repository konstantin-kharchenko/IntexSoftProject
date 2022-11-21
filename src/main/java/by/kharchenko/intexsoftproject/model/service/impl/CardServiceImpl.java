package by.kharchenko.intexsoftproject.model.service.impl;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CardNumberDto;
import by.kharchenko.intexsoftproject.model.entity.LoyaltyCard;
import by.kharchenko.intexsoftproject.model.entity.Percent;
import by.kharchenko.intexsoftproject.model.entity.User;
import by.kharchenko.intexsoftproject.model.repository.LoyaltyCardRepository;
import by.kharchenko.intexsoftproject.model.repository.PercentRepository;
import by.kharchenko.intexsoftproject.model.repository.UserRepository;
import by.kharchenko.intexsoftproject.model.service.CardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CardServiceImpl implements CardService {

    private UserRepository userRepository;
    private PercentRepository percentRepository;
    private LoyaltyCardRepository loyaltyCardRepository;

    @Override
    public void addCard(CardNumberDto cardNumberDto) throws ServiceException {
        Optional<User> optionalUser = userRepository.findByEmail(cardNumberDto.getEmail());
        Optional<Percent> optionalPercent = percentRepository.findById(1L);
        User user = optionalUser.get();
        Integer number = user.hashCode();
        if (number.equals(cardNumberDto.getNumber())) {
            LoyaltyCard loyaltyCard = new LoyaltyCard();
            loyaltyCard.setUser(user);
            loyaltyCard.setNumer(new BigInteger(cardNumberDto.getNumber().toString()));
            loyaltyCard.setPercent(optionalPercent.get());
            loyaltyCardRepository.save(loyaltyCard);
        } else {
            throw new ServiceException("card number not valid");
        }
    }
}
