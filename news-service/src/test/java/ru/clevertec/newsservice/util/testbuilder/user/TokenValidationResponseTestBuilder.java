package ru.clevertec.newsservice.util.testbuilder.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.proto.TokenValidationResponse;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aTokenValidationResponse")
@With
public class TokenValidationResponseTestBuilder implements TestBuilder<TokenValidationResponse> {

    private String role = "ADMIN";
    private String email = "Green@gmail.com";

    @Override
    public TokenValidationResponse build() {
        return TokenValidationResponse.newBuilder()
                .setRole(role)
                .setEmail(email)
                .build();
    }

}
