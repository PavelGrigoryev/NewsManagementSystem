package ru.clevertec.userservice.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.userservice.dto.RegisterRequest;
import ru.clevertec.userservice.model.Role;
import ru.clevertec.userservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aRegisterRequest")
@With
public class RegisterRequestTestBuilder implements TestBuilder<RegisterRequest> {

    private String firstname = "Брюс";
    private String lastname = "Ли";
    private String email = "BruceLee@shazam.com";
    private String password = "777";
    private String role = Role.ADMIN.name();

    @Override
    public RegisterRequest build() {
        return new RegisterRequest(firstname, lastname, email, password, role);
    }

}
