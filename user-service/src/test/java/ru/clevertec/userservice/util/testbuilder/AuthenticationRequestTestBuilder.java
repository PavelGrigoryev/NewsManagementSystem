package ru.clevertec.userservice.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.userservice.dto.AuthenticationRequest;
import ru.clevertec.userservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aAuthenticationRequest")
@With
public class AuthenticationRequestTestBuilder implements TestBuilder<AuthenticationRequest> {

    private String email = "BruceLee@shazam.com";
    private String password = "777";

    @Override
    public AuthenticationRequest build() {
        return new AuthenticationRequest(email, password);
    }

}
