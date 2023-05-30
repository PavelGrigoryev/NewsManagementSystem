package ru.clevertec.newsservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.dto.comment.CommentWithNewsRequest;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.util.json.CommentJsonSupplier;
import ru.clevertec.newsservice.util.json.CommonErrorJsonSupplier;
import ru.clevertec.newsservice.util.json.NewsJsonSupplier;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentWithNewsRequestTestBuilder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class CommentControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            long id = 5;
            String json = CommentJsonSupplier.getCommentResponse();

            mockMvc.perform(get("/comments/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 256;
            String json = CommentJsonSupplier.getNotFoundGetCommentResponse();

            mockMvc.perform(get("/comments/" + wrongId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongId = -1L;
            String json = CommonErrorJsonSupplier.getPositiveErrorGetResponse();

            mockMvc.perform(get("/comments/" + wrongId))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class FindNewsByNewsIdWithCommentsGetEndpointTest {

        @Test
        @DisplayName("test should return news json and status 200 with no comments on the page")
        void testShouldReturnNewsJsonAndStatus200WithNoCommentsOnThePage() throws Exception {
            long newsId = 5;
            int page = 5;

            mockMvc.perform(get("/comments/news/" + newsId + "?page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.comments").isEmpty());
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            long newsId = 5;
            int page = 0;
            String json = CommentJsonSupplier.getNewsWithCommentsResponse();

            mockMvc.perform(get("/comments/news/" + newsId + "?page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 406 if typo in sort")
        void testShouldReturnExpectedJsonAndStatus406IfTypoInSort() throws Exception {
            long newsId = 5;
            int page = 0;
            String typoInSort = "usename";
            String json = CommentJsonSupplier.getTypoInSortCommentResponse();

            mockMvc.perform(get("/comments/news/" + newsId + "?page=" + page + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongNewsId = 122;
            int page = 0;
            String json = NewsJsonSupplier.getNotFoundGetNewsResponse();

            mockMvc.perform(get("/comments/news/" + wrongNewsId + "?page=" + page))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongNewsId = -1;
            int page = 0;
            String json = CommonErrorJsonSupplier.getPositiveErrorGetAllResponse();

            mockMvc.perform(get("/comments/news/" + wrongNewsId + "?page=" + page))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class FindAllByMatchingTextParamsGetEndPointTest {

        @Test
        @DisplayName("test should return empty json and status 200 if there is no comments on the page")
        void testShouldReturnEmptyJsonAndStatus200IfThereIsNoNewsOnThePage() throws Exception {
            int page = 5;
            String username = "Наталья";
            String json = "[]";

            mockMvc.perform(get("/comments/params?username=" + username + "&page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            int page = 0;
            String username = "Ольга";
            String json = CommentJsonSupplier.getMatcherCommentResponse();

            mockMvc.perform(get("/comments/params?username=" + username + "&page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 406 if typo in sort")
        void testShouldReturnExpectedJsonAndStatus406IfTypoInSort() throws Exception {
            int page = 0;
            String username = "Ольга";
            String typoInSort = "usename";
            String json = CommentJsonSupplier.getTypoInSortCommentResponse();

            mockMvc.perform(get("/comments/params?username=" + username + "&page=" + page + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if username is out of pattern")
        void testShouldReturnExpectedJsonAndStatus409IfUsernameIsOutOfPattern() throws Exception {
            int page = 0;
            String username = "Али - Баба";
            String json = CommentJsonSupplier.getPatternCommentErrorResponse();

            mockMvc.perform(get("/comments/params?username=" + username + "&page=" + page))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class SavePostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() throws Exception {
            long expectedId = 26L;
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(expectedId))
                    .andExpect(jsonPath("$.text").value(request.text()))
                    .andExpect(jsonPath("$.username").value(request.username()));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest()
                    .withNewsId(122L)
                    .build();
            String content = objectMapper.writeValueAsString(request);
            String json = NewsJsonSupplier.getNotFoundGetNewsResponse();

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if username is out of pattern")
        void testShouldReturnExpectedJsonAndStatus409IfUserNameIsOutOfPattern() throws Exception {
            CommentWithNewsRequest request = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest()
                    .withUsername("Али - Баба")
                    .build();
            String content = objectMapper.writeValueAsString(request);
            String json = CommentJsonSupplier.getPatternCommentErrorResponse();

            mockMvc.perform(post("/comments")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class UpdateByIdPutEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() throws Exception {
            long id = 5;
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            String content = objectMapper.writeValueAsString(request);

            mockMvc.perform(put("/comments/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.text").value(request.text()))
                    .andExpect(jsonPath("$.username").value(request.username()));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().build();
            String content = objectMapper.writeValueAsString(request);
            String json = CommentJsonSupplier.getNotFoundPutCommentResponse();

            mockMvc.perform(put("/comments/" + wrongId)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if username is out of pattern")
        void testShouldReturnExpectedJsonAndStatus409IfUsernameIsOutOfPattern() throws Exception {
            long id = 5;
            CommentRequest request = CommentRequestTestBuilder.aCommentRequest().withUsername("Али - Баба").build();
            String content = objectMapper.writeValueAsString(request);
            String json = CommentJsonSupplier.getPatternCommentErrorResponse();

            mockMvc.perform(put("/comments/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class DeleteByIdEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            long id = 5;
            DeleteResponse response = new DeleteResponse("Comment with ID " + id + " was successfully deleted");
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(delete("/comments/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            String json = CommentJsonSupplier.getNotFoundDeleteCommentResponse();

            mockMvc.perform(delete("/comments/" + wrongId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongId = -1L;
            String json = CommonErrorJsonSupplier.getPositiveErrorDeleteResponse();

            mockMvc.perform(delete("/comments/" + wrongId))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

}
