package ru.clevertec.newsservice.util.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface CommonErrorJsonSupplier {

    static String getNotBlankErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/not-blank-err-response.json"));
    }

    static String getPositiveErrorGetResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/positive-err-get-response.json"));
    }

    static String getPositiveErrorDeleteResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/positive-err-delete-response.json"));
    }

    static String getPositiveErrorGetAllResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/positive-err-get-all-response.json"));
    }

}
