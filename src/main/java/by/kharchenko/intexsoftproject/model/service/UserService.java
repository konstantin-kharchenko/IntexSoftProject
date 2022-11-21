package by.kharchenko.intexsoftproject.model.service;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CustomTokenDto;
import by.kharchenko.intexsoftproject.model.dto.RegisterUserDto;
import by.kharchenko.intexsoftproject.model.dto.SignInUserDto;
import by.kharchenko.intexsoftproject.model.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {
    Optional<CustomTokenDto> signIn(SignInUserDto signInUserDto) throws ServiceException;

    void register(RegisterUserDto registerUserDto) throws ServiceException;

    Optional<CustomTokenDto> refresh(String refreshToken) throws ServiceException;

    Optional<UserDto> findById(Long id) throws ServiceException;

    void addPhoto(MultipartFile file, Long id) throws ServiceException;
}
