package ru.clevertec.newsservice.util.testbuilder.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.user.AuthenticationRequest;
import ru.clevertec.newsservice.util.TestBuilder;

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
