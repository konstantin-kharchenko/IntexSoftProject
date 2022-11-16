package by.kharchenko.intexsoftproject.model.service.impl;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.CustomTokenDto;
import by.kharchenko.intexsoftproject.model.dto.RegisterUserDto;
import by.kharchenko.intexsoftproject.model.dto.SignInUserDto;
import by.kharchenko.intexsoftproject.model.dto.UserDto;
import by.kharchenko.intexsoftproject.model.entity.Role;
import by.kharchenko.intexsoftproject.model.entity.RoleType;
import by.kharchenko.intexsoftproject.model.entity.User;
import by.kharchenko.intexsoftproject.model.mapper.UserMapper;
import by.kharchenko.intexsoftproject.model.repository.RoleRepository;
import by.kharchenko.intexsoftproject.model.repository.UserRepository;
import by.kharchenko.intexsoftproject.model.service.UserService;
import by.kharchenko.intexsoftproject.security.JwtTokenProvider;
import by.kharchenko.intexsoftproject.security.JwtType;
import by.kharchenko.intexsoftproject.util.encryption.EncryptionPassword;
import by.kharchenko.intexsoftproject.util.mail.CustomMailSender;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomMailSender mailSender;

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Optional<CustomTokenDto> signIn(SignInUserDto signInUserDto) throws ServiceException {
        Optional<User> optionalUser = userRepository.findByUsername(signInUserDto.getUsername());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String encryptionPassword = EncryptionPassword.encryption(signInUserDto.getPassword());
            if (encryptionPassword.equals(user.getPassword())) {
                CustomTokenDto customTokenDto = new CustomTokenDto();
                String accessToken = jwtTokenProvider.createAccessToken(user);
                String refreshToken = jwtTokenProvider.createRefreshToken(user);

                customTokenDto.setAccessToken(accessToken);
                customTokenDto.setRefreshToken(refreshToken);

                return Optional.of(customTokenDto);
            } else {
                return Optional.empty();
            }
        }
        throw new UsernameNotFoundException("User with username: " + signInUserDto.getUsername() + " not found");
    }

    @Override
    public boolean signUp(RegisterUserDto registerUserDto) throws ServiceException {
        User user = UserMapper.INSTANCE.registerUserDtoToUser(registerUserDto);
        String encryptionPassword = EncryptionPassword.encryption(user.getPassword());
        user.setPassword(encryptionPassword);
        Set<Role> roles = new HashSet<>();
        Optional<Role> role = roleRepository.findByName(RoleType.ROLE_USER);
        roles.add(role.get());
        user.setRoles(roles);
        boolean isLoginExists = userRepository.findByUsername(registerUserDto.getUsername()).isPresent();
        if (isLoginExists) {
            throw new ServiceException("this login is already exists");
        }
        userRepository.save(user);
        mailSender.sendCustomEmail(user.getEmail(),"REGISTRATION MESSAGE", "You are successfully register in IntexSoftApp");
        return true;
    }

    @Override
    public Optional<CustomTokenDto> refresh(String refreshToken) throws ServiceException {
        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken, JwtType.REFRESH)) {
            Long userId = Long.parseLong(jwtTokenProvider.getRefreshClaims(refreshToken).getSubject());
            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                String accessToken = jwtTokenProvider.createAccessToken(user);
                String newRefreshToken = jwtTokenProvider.createRefreshToken(user);
                CustomTokenDto tokenDto = new CustomTokenDto(accessToken, newRefreshToken);
                return Optional.of(tokenDto);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDto> getUserById(Long id) throws ServiceException {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            UserDto userDto = UserMapper.INSTANCE.userToUserDto(user.get());
            return Optional.of(userDto);
        }
        throw new ServiceException("User not found");
    }
}
