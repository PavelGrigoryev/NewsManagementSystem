package ru.clevertec.userservice.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.userservice.dto.UserRegisterRequest;
import ru.clevertec.userservice.dto.UserResponse;
import ru.clevertec.userservice.model.User;

@Mapper
public interface UserMapper {

    User fromRequest(UserRegisterRequest userRegisterRequest);

    UserResponse toResponse(User user, String token, String tokenExpiration);

}
