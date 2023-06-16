package ru.clevertec.newsservice.util.testbuilder.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.proto.UserAuthenticationRequest;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserAuthenticationRequest")
@With
public class UserAuthenticationRequestTestBuilder implements TestBuilder<UserAuthenticationRequest> {

    private String email = "BruceLee@shazam.com";
    private String password = "777";

    @Override
    public UserAuthenticationRequest build() {
        return UserAuthenticationRequest.newBuilder()
                .setEmail(email)
                .setPassword(password)
                .build();
    }

}
