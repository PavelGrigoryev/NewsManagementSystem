package ru.clevertec.newsservice.util.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface AuthenticationJsonSupplier {

    static String getPatternRoleErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/auth/pattern-of-role-err-response.json"));
    }

    static String getUniqueEmailErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/auth/unique-email-err-response.json"));
    }

    static String getWrongPasswordErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/auth/wrong-password-err-response.json"));
    }

    static String getNotFoundErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/auth/404-err-response.json"));
    }

    static String getPatternEmailErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/auth/pattern-email-err-response.json"));
    }

    static String getPatternFirstNameErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/auth/pattern-firstname-err-response.json"));
    }

}
