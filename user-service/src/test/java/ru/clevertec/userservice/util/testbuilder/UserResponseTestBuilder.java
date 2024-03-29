package ru.clevertec.userservice.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.userservice.model.Role;
import ru.clevertec.userservice.dto.proto.UserResponse;
import ru.clevertec.userservice.util.TestBuilder;

import java.time.LocalDateTime;
import java.time.Month;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserResponse")
@With
public class UserResponseTestBuilder implements TestBuilder<UserResponse> {

    private Long id = 3L;
    private String firstname = "Bruce";
    private String lastname = "Lee";
    private String email = "BruceLee@shazam.com";
    private Role role = Role.ADMIN;
    private String token = "jwt";
    private String tokenExpiration = "Mon Jun 12 15:52:45 MSK 2023";
    private LocalDateTime createdTime = LocalDateTime.of(2023, Month.MAY, 25, 15, 25, 33);
    private LocalDateTime updatedTime = LocalDateTime.of(2023, Month.JUNE, 6, 11, 17, 21);

    @Override
    public UserResponse build() {
        return UserResponse.newBuilder()
                .setId(id)
                .setFirstname(firstname)
                .setLastname(lastname)
                .setEmail(email)
                .setRole(role.name())
                .build();
    }

}
