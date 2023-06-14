package ru.clevertec.userservice.util.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface UserJsonSupplier {

    static String getUniqueEmailErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/unique-email-err-response.json"));
    }

    static String getPatternRoleErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/pattern-role-err-response.json"));
    }

    static String getPatternEmailErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/pattern-email-err-response.json"));
    }

    static String getPatternFirstnameErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/pattern-firstname-err-response.json"));
    }

    static String getWrongPasswordErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/wrong-password-err-response.json"));
    }

    static String getNotFoundErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/404-err-response.json"));
    }

    static String getNotFoundUpdateErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/404-update-err-response.json"));
    }

    static String getNotFoundDeleteErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/404-delete-err-response.json"));
    }

    static String getMalformedErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/malformed-err-response.json"));
    }

    static String getSignatureErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/signature-err-response.json"));
    }

}
