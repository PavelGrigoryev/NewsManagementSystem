package ru.clevertec.userservice.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.userservice.dto.proto.TokenValidationResponse;
import ru.clevertec.userservice.util.TestBuilder;

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
