package ru.clevertec.userservice.util.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface TokenTxtSupplier {

    static String getToken2048Request() throws IOException {
        return Files.readString(Paths.get("src/test/resources/txt/token2048.txt"));
    }

    static String getBadSignatureTokenRequest() throws IOException {
        return Files.readString(Paths.get("src/test/resources/txt/bad-signature-token.txt"));
    }

    static String getExpiredTokenRequest() throws IOException {
        return Files.readString(Paths.get("src/test/resources/txt/expired-token.txt"));
    }

    static String getDeletedEmailTokenRequest() throws IOException {
        return Files.readString(Paths.get("src/test/resources/txt/deleted-email-token.txt"));
    }

}
