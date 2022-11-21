package by.kharchenko.intexsoftproject.model.service.impl;

import by.kharchenko.intexsoftproject.exception.ServiceException;
import by.kharchenko.intexsoftproject.model.dto.*;
import by.kharchenko.intexsoftproject.model.entity.Role;
import by.kharchenko.intexsoftproject.model.entity.RoleType;
import by.kharchenko.intexsoftproject.model.entity.User;
import by.kharchenko.intexsoftproject.model.mapper.UserMapper;
import by.kharchenko.intexsoftproject.model.repository.RoleRepository;
import by.kharchenko.intexsoftproject.model.repository.UserRepository;
import by.kharchenko.intexsoftproject.model.service.UserService;
import by.kharchenko.intexsoftproject.security.JwtTokenProvider;
import by.kharchenko.intexsoftproject.security.JwtType;
import by.kharchenko.intexsoftproject.util.encryption.CustomPictureEncoder;
import by.kharchenko.intexsoftproject.util.encryption.EncryptionPassword;
import by.kharchenko.intexsoftproject.util.filereadwrite.FileReaderWriter;
import by.kharchenko.intexsoftproject.util.mail.CustomMailSender;
import by.kharchenko.intexsoftproject.util.validator.PhotoValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    public static final String PHOTO_PATH_ON_HDD = "D:\\FINAL_PROJECT\\PHOTO\\CLIENT\\";
    private static final String FILE_EXTENSION = ".txt";
    private final FileReaderWriter readerWriter;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomMailSender mailSender;
    private final JwtTokenProvider jwtTokenProvider;
    private final PhotoValidator photoValidator;

    @Override
    public Optional<CustomTokenDto> signIn(SignInUserDto signInUserDto) throws ServiceException {
        Optional<User> optionalUser = userRepository.findByUsernameOrEmail(signInUserDto.getData());
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
                log.info("Incorrect login or password");
                throw new ServiceException("Incorrect login or password");
            }
        }
        log.info("User with data: " + signInUserDto.getData() + " not found");
        throw new UsernameNotFoundException("User with data: " + signInUserDto.getData() + " not found");
    }

    @Override
    public void register(RegisterUserDto registerUserDto) throws ServiceException {
        boolean isLoginExists = userRepository.findByUsername(registerUserDto.getUsername()).isPresent();
        if (isLoginExists) {
            log.info("username: " + registerUserDto.getUsername() + "is already exists");
            throw new ServiceException("this username is already exists");
        }
        boolean isEmailExists = userRepository.findByEmail(registerUserDto.getEmail()).isPresent();
        if (isEmailExists) {
            log.info("email: " + registerUserDto.getEmail() + "is already exists");
            throw new ServiceException("this emil is already exists");
        }
        User user = UserMapper.INSTANCE.registerUserDtoToUser(registerUserDto);
        String encryptionPassword = EncryptionPassword.encryption(user.getPassword());
        user.setPassword(encryptionPassword);
        Set<Role> roles = new HashSet<>();
        Optional<Role> role = roleRepository.findByName(RoleType.ROLE_USER);
        roles.add(role.get());
        user.setRoles(roles);
        userRepository.save(user);
        mailSender.sendCustomEmail(user.getEmail(), "REGISTRATION MESSAGE", "You are successfully register in IntexSoftApp");
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
        log.info("Token invalid");
        throw new ServiceException("Token invalid");
    }

    @Override
    public Optional<UserDto> findById(Long id) throws ServiceException {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDto userDto = UserMapper.INSTANCE.userToUserDto(user);
            if (user.getPhotoPath() != null) {
                String stringPhoto = readerWriter.readPhoto(user.getPhotoPath());
               userDto.setPhoto(stringPhoto);
            }
            return Optional.of(userDto);
        }
        log.info("User not found");
        throw new ServiceException("User not found");
    }

    @Override
    public void addPhoto(MultipartFile file, Long id) throws ServiceException {
        try {
            String name = file.getOriginalFilename();
            boolean isCorrect = photoValidator.isCorrect(name);
            if (isCorrect) {
                byte[] photoBytes = file.getBytes();
                String userPhoto = CustomPictureEncoder.arrayToBase64(photoBytes);
                String path = PHOTO_PATH_ON_HDD + id + FILE_EXTENSION;
                boolean isWrite = readerWriter.writePhoto(userPhoto, path);
                if (isWrite) {
                    userRepository.savePhoto(path, id);
                }
            }
            log.info("file have to picture");
            throw new ServiceException("file have to picture");
        } catch (IOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void generateNumber(GenerateCardUserDto generateCardUserDto) throws ServiceException {
        Optional<User> user = userRepository.findByEmail(generateCardUserDto.getEmail());
        if (user.isPresent()) {
            Integer number = user.get().hashCode();
            mailSender.sendCustomEmail(generateCardUserDto.getEmail(), "CARD NUMBER", "Hello, your card number is: " + number);
        }
        else {
            log.info("user with this email not found");
            throw new ServiceException("user with this email not found");
        }
    }
}
