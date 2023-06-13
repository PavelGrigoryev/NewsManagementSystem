package ru.clevertec.newsservice.util.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface CommonErrorJsonSupplier {

    static String getSizeErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/size-err-response.json"));
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

    static String getUnauthorizedErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/401-err-response.json"));
    }

    static String getForbiddenForJournalistErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/403-for-journalist-err-response.json"));
    }

    static String getForbiddenForSubscriberErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/403-for-subscriber-err-response.json"));
    }

    static String getMethodNotAllowedForSubscriberErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/405-for-subscriber-err-response.json"));
    }

    static String getMethodNotAllowedForJournalistErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/error/405-for-journalist-err-response.json"));
    }

}
