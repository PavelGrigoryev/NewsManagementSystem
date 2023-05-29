package ru.clevertec.newsservice.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface JsonSupplier {

    static String getNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/news-response.json"));
    }

    static String getNotFoundNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/404-news-response.json"));
    }

    static String getPositiveErrorNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/positive-err-news-response.json"));
    }

    static String getAllNewsResponses() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/all-news-responses.json"));
    }

    static String getTypoInSortNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/typo-in-sort-news-response.json"));
    }

    static String getMatcherNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/matcher-news-response.json"));
    }

    static String getNotBlankErrorNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/not-blank-err-news-response.json"));
    }

}
