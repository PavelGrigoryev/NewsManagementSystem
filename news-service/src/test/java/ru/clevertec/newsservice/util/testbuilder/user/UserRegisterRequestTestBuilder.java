package ru.clevertec.newsservice.util.testbuilder.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.user.UserRegisterRequest;
import ru.clevertec.newsservice.dto.user.Role;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserRegisterRequest")
@With
public class UserRegisterRequestTestBuilder implements TestBuilder<UserRegisterRequest> {

    private String firstname = "Брюс";
    private String lastname = "Ли";
    private String email = "BruceLee@shazam.com";
    private String password = "777";
    private String role = Role.ADMIN.name();

    @Override
    public UserRegisterRequest build() {
        return new UserRegisterRequest(firstname, lastname, email, password, role);
    }

}
