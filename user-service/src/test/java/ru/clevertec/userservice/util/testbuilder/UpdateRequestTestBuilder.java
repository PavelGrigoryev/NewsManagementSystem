package ru.clevertec.userservice.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import ru.clevertec.userservice.dto.UpdateRequest;
import ru.clevertec.userservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aUpdateRequest")
@With
public class UpdateRequestTestBuilder implements TestBuilder<UpdateRequest> {

    private String firstname = "Samuel";
    private String lastname = "Ilf";
    private String password = "1984";

    @Override
    public UpdateRequest build() {
        return new UpdateRequest(firstname, lastname, password);
    }

}
