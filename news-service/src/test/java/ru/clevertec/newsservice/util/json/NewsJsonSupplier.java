package ru.clevertec.newsservice.util.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface NewsJsonSupplier {

    static String getNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/news/news-response.json"));
    }

    static String getAllNewsResponses() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/news/all-news-responses.json"));
    }

    static String getNotFoundGetNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/news/404-get-news-response.json"));
    }

    static String getNotFoundPutNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/news/404-put-news-response.json"));
    }

    static String getNotFoundDeleteNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/news/404-delete-news-response.json"));
    }

    static String getTypoInSortNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/news/typo-in-sort-news-response.json"));
    }

    static String getMatcherNewsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/news/matcher-news-response.json"));
    }

}
