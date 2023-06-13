package ru.clevertec.userservice.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.userservice.dto.UserAuthenticationRequest;
import ru.clevertec.userservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserAuthenticationRequest")
@With
public class UserAuthenticationRequestTestBuilder implements TestBuilder<UserAuthenticationRequest> {

    private String email = "BruceLee@shazam.com";
    private String password = "777";

    @Override
    public UserAuthenticationRequest build() {
        return new UserAuthenticationRequest(email, password);
    }

}
