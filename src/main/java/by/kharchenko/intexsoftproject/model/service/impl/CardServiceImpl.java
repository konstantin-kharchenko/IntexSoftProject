package by.kharchenko.intexsoftproject.model.service.impl;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CardNumberDto;
import by.kharchenko.intexsoftproject.model.dto.GenerateCardUserDto;
import by.kharchenko.intexsoftproject.model.entity.CardNumber;
import by.kharchenko.intexsoftproject.model.entity.LoyaltyCard;
import by.kharchenko.intexsoftproject.model.entity.Percent;
import by.kharchenko.intexsoftproject.model.entity.User;
import by.kharchenko.intexsoftproject.model.repository.CardNumberRepository;
import by.kharchenko.intexsoftproject.model.repository.LoyaltyCardRepository;
import by.kharchenko.intexsoftproject.model.repository.PercentRepository;
import by.kharchenko.intexsoftproject.model.repository.UserRepository;
import by.kharchenko.intexsoftproject.model.service.CardService;
import by.kharchenko.intexsoftproject.util.mail.CustomMailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CardServiceImpl implements CardService {

    private UserRepository userRepository;
    private PercentRepository percentRepository;
    private LoyaltyCardRepository loyaltyCardRepository;
    private CardNumberRepository cardNumberRepository;
    private final CustomMailSender mailSender;


    @Override
    public void generateNumber(GenerateCardUserDto generateCardUserDto) throws ServiceException {
        Optional<User> user = userRepository.findByEmail(generateCardUserDto.getEmail());
        if (user.isPresent()) {
            UUID uuid = UUID.randomUUID();
            CardNumber cardNumber = new CardNumber();
            cardNumber.setNumber(uuid.toString());
            cardNumber.setUser(user.get());
            cardNumberRepository.save(cardNumber);
            mailSender.sendCustomEmail(generateCardUserDto.getEmail(), "CARD NUMBER", "Hello, your card number is: " + uuid);
        } else {
            log.info("user with this email not found");
            throw new ServiceException("user with this email not found");
        }
    }

    @Override
    public void addCard(CardNumberDto cardNumberDto) throws ServiceException {
        Optional<User> optionalUser = userRepository.findByEmail(cardNumberDto.getEmail());
        Optional<Percent> optionalPercent = percentRepository.findById(1L);
        User user = optionalUser.get();
        Optional<CardNumber> optionalCardNumber = cardNumberRepository.findByUser(user);
        CardNumber cardNumber = optionalCardNumber.get();
        if (cardNumber.getNumber().equals(cardNumberDto.getNumber())) {
            LoyaltyCard loyaltyCard = new LoyaltyCard();
            loyaltyCard.setUser(user);
            loyaltyCard.setCardNumber(cardNumber);
            loyaltyCard.setPercent(optionalPercent.get());
            loyaltyCardRepository.save(loyaltyCard);
        } else {
            throw new ServiceException("card number not valid");
        }
    }
}
