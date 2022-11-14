package by.kharchenko.intexsoftproject.model.mapper;

import by.kharchenko.intexsoftproject.model.dto.RegisterUserDto;
import by.kharchenko.intexsoftproject.model.dto.SignInUserDto;
import by.kharchenko.intexsoftproject.model.dto.UserDto;
import by.kharchenko.intexsoftproject.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDTO);

    RegisterUserDto userToRegisterUserDto(User user);

    User registerUserDtoToUser(RegisterUserDto registerUserDto);

    SignInUserDto userToSignInUserDto(User user);

    User signInUserDtoToUser(SignInUserDto signInUserDto);

    List<UserDto> listUserTOListUserDto(List<User> users);
}

