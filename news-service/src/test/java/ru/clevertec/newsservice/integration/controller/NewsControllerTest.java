package ru.clevertec.newsservice.integration.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.newsservice.integration.BaseIntegrationTest;
import ru.clevertec.newsservice.util.JsonSupplier;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@RequiredArgsConstructor
class NewsControllerTest extends BaseIntegrationTest {

    private final MockMvc mockMvc;

    @Nested
    class FindByIdGetEndpointTest {

        @Test
        @DisplayName("test should return expected json and status 200")
        void testShouldReturnExpectedJsonAndStatus200() throws Exception {
            long id = 5;
            String json = JsonSupplier.getNewsResponse();

            mockMvc.perform(get("/news/" + id))
                    .andExpect(status().isOk())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 404")
        void testShouldReturnExpectedJsonAndStatus404() throws Exception {
            long wrongId = 122;
            String json = JsonSupplier.getNotFoundNewsResponse();

            mockMvc.perform(get("/news/" + wrongId))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() throws Exception {
            long wrongId = -1;
            String json = JsonSupplier.getPositiveErrorNewsResponse();

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
            String json = JsonSupplier.getAllNewsResponses();

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
            String json = JsonSupplier.getTypoInSortNewsResponse();

            mockMvc.perform(get("/news?page=" + page + "&size=" + size + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

    }

    @Nested
    class FindAllByMatchingTextParams {

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
            String json = JsonSupplier.getMatcherNewsResponse();

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
            String json = JsonSupplier.getTypoInSortNewsResponse();

            mockMvc.perform(get("/news/params?text=" + text + "&title=" + title + "&sort=" + typoInSort))
                    .andExpect(status().isNotAcceptable())
                    .andExpect(content().json(json));
        }

        @Test
        @DisplayName("test should return expected json and status 409")
        void testShouldReturnExpectedJsonAndStatus409() throws Exception {
            String text = "";
            String title = "в россии";
            String json = JsonSupplier.getNotBlankErrorNewsResponse();

            mockMvc.perform(get("/news/params?text=" + text + "&title=" + title))
                    .andExpect(status().isConflict())
                    .andExpect(content().json(json));
        }

    }

}
