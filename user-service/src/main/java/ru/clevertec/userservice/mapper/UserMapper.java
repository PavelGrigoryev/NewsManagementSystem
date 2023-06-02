package ru.clevertec.userservice.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.userservice.dto.RegisterRequest;
import ru.clevertec.userservice.dto.UserResponse;
import ru.clevertec.userservice.model.User;

@Mapper
public interface UserMapper {

    User fromRequest(RegisterRequest registerRequest);

    UserResponse toResponse(User user, String token, String tokenExpiration);

}
