package by.kharchenko.intexsoftproject.model.service;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CustomTokenDto;
import by.kharchenko.intexsoftproject.model.dto.RegisterUserDto;
import by.kharchenko.intexsoftproject.model.dto.SignInUserDto;
import by.kharchenko.intexsoftproject.model.dto.UserDto;

import java.util.Optional;

public interface UserService {
    Optional<CustomTokenDto> signIn(SignInUserDto signInUserDto) throws ServiceException;

    boolean signUp(RegisterUserDto registerUserDto) throws ServiceException;

    Optional<CustomTokenDto> refresh(String refreshToken) throws ServiceException;

    Optional<UserDto> getUserById(Long id) throws ServiceException;
}
