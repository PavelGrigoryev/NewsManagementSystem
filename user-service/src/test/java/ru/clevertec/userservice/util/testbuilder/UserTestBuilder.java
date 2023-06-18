package ru.clevertec.userservice.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.userservice.model.Role;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.util.TestBuilder;

import java.time.LocalDateTime;
import java.time.Month;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUser")
@With
public class UserTestBuilder implements TestBuilder<User> {

    private Long id = 3L;
    private String firstname = "Bruce";
    private String lastname = "Lee";
    private String email = "BruceLee@shazam.com";
    private String password = "777";
    private Role role = Role.ADMIN;
    private LocalDateTime createdTime = LocalDateTime.of(2023, Month.MAY, 25, 15, 25, 33);
    private LocalDateTime updatedTime = LocalDateTime.of(2023, Month.JUNE, 6, 11, 17, 21);

    @Override
    public User build() {
        return User.builder()
                .id(id)
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .password(password)
                .role(role)
                .createdTime(createdTime)
                .updatedTime(updatedTime)
                .build();
    }

}
