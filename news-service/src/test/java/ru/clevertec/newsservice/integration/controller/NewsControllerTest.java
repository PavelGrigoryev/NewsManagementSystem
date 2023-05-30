package ru.clevertec.newsservice.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.util.json.CommonErrorJsonSupplier;
import ru.clevertec.newsservice.util.json.NewsJsonSupplier;
import ru.clevertec.newsservice.util.testbuilder.news.NewsRequestTestBuilder;

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
class NewsControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            long id = 5;
            String json = NewsJsonSupplier.getNewsResponse();

            mockMvc.perform(get("/news/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            String json = NewsJsonSupplier.getNotFoundGetNewsResponse();

            mockMvc.perform(get("/news/" + wrongId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongId = -1L;
            String json = CommonErrorJsonSupplier.getPositiveErrorGetResponse();

            mockMvc.perform(get("/news/" + wrongId))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class FindAllGetEndpointTest {

        @Test
        @DisplayName("test should return empty json and status 200 if there is no news on the page")
        void testShouldReturnEmptyJsonAndStatus200IfThereIsNoNewsOnThePage() throws Exception {
            int page = 3;
            String json = "[]";

            mockMvc.perform(get("/news?page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            int page = 0;
            int size = 5;
            String json = NewsJsonSupplier.getAllNewsResponses();

            mockMvc.perform(get("/news?page=" + page + "&size=" + size))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 406 if typo in sort")
        void testShouldReturnExpectedJsonAndStatus406IfTypoInSort() throws Exception {
            int page = 0;
            int size = 5;
            String typoInSort = "tit";
            String json = NewsJsonSupplier.getTypoInSortNewsResponse();

            mockMvc.perform(get("/news?page=" + page + "&size=" + size + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class FindAllByMatchingTextParamsGetEndPointTest {

        @Test
        @DisplayName("test should return empty json and status 200 if there is no news on the page")
        void testShouldReturnEmptyJsonAndStatus200IfThereIsNoNewsOnThePage() throws Exception {
            int page = 3;
            String text = "в России";
            String title = "в россии";
            String json = "[]";

            mockMvc.perform(get("/news/params?text=" + text + "&title=" + title + "&page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            int page = 0;
            String text = "в России";
            String title = "в россии";
            String json = NewsJsonSupplier.getMatcherNewsResponse();

            mockMvc.perform(get("/news/params?text=" + text + "&title=" + title + "&page=" + page))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 406 if typo in sort")
        void testShouldReturnExpectedJsonAndStatus406IfTypoInSort() throws Exception {
            String text = "в России";
            String title = "в россии";
            String typoInSort = "tit";
            String json = NewsJsonSupplier.getTypoInSortNewsResponse();

            mockMvc.perform(get("/news/params?text=" + text + "&title=" + title + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if text is blank")
        void testShouldReturnExpectedJsonAndStatus409IfTextIsBlank() throws Exception {
            String text = "";
            String title = "в россии";
            String json = CommonErrorJsonSupplier.getNotBlankErrorResponse();

            mockMvc.perform(get("/news/params?text=" + text + "&title=" + title))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class SavePostEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 201")
        void testShouldReturnExpectedJsonAndStatus201() throws Exception {
            long expectedId = 6L;
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(newsRequest);

            mockMvc.perform(post("/news")
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(expectedId))
                    .andExpect(jsonPath("$.title").value(newsRequest.title()))
                    .andExpect(jsonPath("$.text").value(newsRequest.text()));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if text is blank")
        void testShouldReturnExpectedJsonAndStatus409IfTitleIsBlank() throws Exception {
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().withText("").build();
            String content = objectMapper.writeValueAsString(newsRequest);
            String json = CommonErrorJsonSupplier.getNotBlankErrorResponse();

            mockMvc.perform(post("/news")
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
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(newsRequest);

            mockMvc.perform(put("/news/" + id)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(id))
                    .andExpect(jsonPath("$.title").value(newsRequest.title()))
                    .andExpect(jsonPath("$.text").value(newsRequest.text()));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            String content = objectMapper.writeValueAsString(newsRequest);
            String json = NewsJsonSupplier.getNotFoundPutNewsResponse();

            mockMvc.perform(put("/news/" + wrongId)
                            .content(content)
                            .contentType(APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if text is blank")
        void testShouldReturnExpectedJsonAndStatus409IfTitleIsBlank() throws Exception {
            long id = 5;
            NewsRequest newsRequest = NewsRequestTestBuilder.aNewsRequest().withText("").build();
            String content = objectMapper.writeValueAsString(newsRequest);
            String json = CommonErrorJsonSupplier.getNotBlankErrorResponse();

            mockMvc.perform(put("/news/" + id)
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
            DeleteResponse response = new DeleteResponse("News with ID " + id + " was successfully deleted");
            String json = objectMapper.writeValueAsString(response);

            mockMvc.perform(delete("/news/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404 if value is not exist in db")
        void testShouldReturnExpectedJsonAndStatus404IfValueIsNotExist() throws Exception {
            long wrongId = 122;
            String json = NewsJsonSupplier.getNotFoundDeleteNewsResponse();

            mockMvc.perform(delete("/news/" + wrongId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409 if id is not positive")
        void testShouldReturnExpectedJsonAndStatus409IfIsNotPositive() throws Exception {
            long wrongId = -1L;
            String json = CommonErrorJsonSupplier.getPositiveErrorDeleteResponse();

            mockMvc.perform(delete("/news/" + wrongId))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

}
