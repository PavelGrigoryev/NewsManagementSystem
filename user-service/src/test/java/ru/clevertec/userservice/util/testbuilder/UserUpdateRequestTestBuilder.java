package ru.clevertec.userservice.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.userservice.dto.UserUpdateRequest;
import ru.clevertec.userservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUserUpdateRequest")
@With
public class UserUpdateRequestTestBuilder implements TestBuilder<UserUpdateRequest> {

    private String firstname = "Samuel";
    private String lastname = "Ilf";
    private String password = "1984";

    @Override
    public UserUpdateRequest build() {
        return new UserUpdateRequest(firstname, lastname, password);
    }

}
