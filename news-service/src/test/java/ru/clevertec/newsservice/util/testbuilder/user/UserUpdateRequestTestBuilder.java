package ru.clevertec.newsservice.util.testbuilder.user;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.newsservice.dto.proto.UserUpdateRequest;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserUpdateRequest")
@With
public class UserUpdateRequestTestBuilder implements TestBuilder<UserUpdateRequest> {

    private String firstname = "Samuel";
    private String lastname = "Ilf";
    private String password = "1984";

    @Override
    public UserUpdateRequest build() {
        return UserUpdateRequest.newBuilder()
                .setFirstname(firstname)
                .setLastname(lastname)
                .setPassword(password)
                .build();
    }

}
