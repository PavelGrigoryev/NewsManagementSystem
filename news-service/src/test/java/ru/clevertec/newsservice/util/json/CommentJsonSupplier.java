package ru.clevertec.newsservice.util.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public interface CommentJsonSupplier {

    static String getCommentResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/comment/comment-response.json"));
    }

    static String getNotFoundGetCommentResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/comment/404-get-comment-response.json"));
    }

    static String getNotFoundPutCommentResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/comment/404-put-comment-response.json"));
    }

    static String getNotFoundDeleteCommentResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/comment/404-delete-comment-response.json"));
    }

    static String getNewsWithCommentsResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/comment/news-with-comments-response.json"));
    }

    static String getTypoInSortCommentResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/comment/typo-in-sort-comment-response.json"));
    }

    static String getMatcherCommentResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/comment/matcher-comment-response.json"));
    }

    static String getPatternCommentErrorResponse() throws IOException {
        return Files.readString(Paths.get("src/test/resources/json/comment/pattern-err-comment-response.json"));
    }

}
