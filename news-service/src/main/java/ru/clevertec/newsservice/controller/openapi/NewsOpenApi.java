package ru.clevertec.newsservice.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.exceptionhandlerstarter.model.IncorrectData;
import ru.clevertec.exceptionhandlerstarter.model.ValidationErrorResponse;
import ru.clevertec.newsservice.dto.news.NewsUpdateRequest;

import java.util.List;

@Validated
@Tag(name = "News", description = "The News Api")
public interface NewsOpenApi {

    @Operation(summary = "Find News by id.", tags = "News",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "19"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class), examples = @ExampleObject("""
                            {
                               "id": 19,
                               "time": "2023-05-27T10:55:59",
                               "title": "В Беларуси прошли массовые акции протеста против алкоголизма",
                               "text": "В Беларуси прошли массовые акции протеста против алкоголизма, который удерживает власть над алкоголиками более 40 лет."
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No News with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchNewsException",
                              "errorMessage": "News with ID 122 does not exist",
                              "errorCode": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "errorMessage": "409 CONFLICT",
                                      "violations": [
                                        {
                                          "fieldName": "findById.id",
                                          "message": "must be greater than 0"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<NewsResponse> findById(@Positive Long id);

    @Operation(summary = "Find all News with pagination.", tags = "News", parameters = {
            @Parameter(name = "page", description = "Enter your page number here", example = "1"),
            @Parameter(name = "size", description = "Enter your page size here", example = "5"),
            @Parameter(name = "sort", description = "Enter your sort by(id, time, title or text) here",
                    schema = @Schema(type = "array", example = "[\"id\"]"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of News retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class), examples = @ExampleObject("""
                            [
                               {
                                 "id": 6,
                                 "time": "2023-05-22T14:12:59",
                                 "title": "В США произошло крупнейшее нарушение данных в истории",
                                 "text": "В США произошло крупнейшее нарушение данных в истории, когда хакеры получили доступ к личной информации миллионов пользователей."
                               },
                               {
                                 "id": 7,
                                 "time": "2023-05-23T12:55:59",
                                 "title": "Европейский союз заключил сделку о свободной торговле с Великобританией",
                                 "text": "Европейский союз и Великобритания заключили сделку о свободной торговле, которая позволит сохранить экономические связи после Brexit."
                               },
                               {
                                 "id": 8,
                                 "time": "2023-05-23T19:46:59",
                                 "title": "В Китае началась строительство самой высокой в мире башни",
                                 "text": "В Китае началось строительство самой высокой в мире башни, которая будет иметь высоту более 1 километра."
                               },
                               {
                                 "id": 9,
                                 "time": "2023-05-23T23:33:59",
                                 "title": "Сирийская армия начала операцию против террористов в Идлибе",
                                 "text": "Сирийская армия начала операцию против террористов в Идлибе, где находится последний оплот боевиков в стране."
                               },
                               {
                                 "id": 10,
                                 "time": "2023-05-24T11:16:59",
                                 "title": "В Индии установлен новый рекорд по числу зараженных COVID-19 за сутки",
                                 "text": "В Индии за сутки было зафиксировано более 300 тысяч новых случаев заражения COVID-19, что стало новым рекордом"
                               }
                            ]
                            """))),
            @ApiResponse(responseCode = "406", description = "Pageable wrong sort params",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "PropertyReferenceException",
                               "errorMessage": "No property 'tit' found for type 'News'; Did you mean 'id','text','time','title'",
                               "errorCode": "406 NOT_ACCEPTABLE"
                            }
                            """))),
    })
    ResponseEntity<List<NewsResponse>> findAll(@ParameterObject Pageable pageable);

    @Operation(summary = "Find all News by matching text and title params with pagination.",
            tags = "News", parameters = {
            @Parameter(name = "text", description = "Enter your text here", example = "в России"),
            @Parameter(name = "title", description = "Enter your title here", example = "в россии"),
            @Parameter(name = "page", description = "Enter your page number here", example = "0"),
            @Parameter(name = "size", description = "Enter your page size here", example = "2"),
            @Parameter(name = "sort", description = "Enter your sort by(id, time, title or text) here",
                    schema = @Schema(type = "array", example = "[\"id,desc\"]"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of News retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class), examples = @ExampleObject("""
                            [
                               {
                                 "id": 13,
                                 "time": "2023-05-25T10:30:59",
                                 "title": "В России запущен новый спутник для наблюдения за климатом",
                                 "text": "В России запущен новый спутник для наблюдения за климатом, который поможет улучшить прогнозы погоды и изменения климата."
                               },
                               {
                                 "id": 4,
                                 "time": "2023-05-22T10:30:59",
                                 "title": "В России началась вакцинация от COVID-19",
                                 "text": "В России началась массовая вакцинация населения от COVID-19, которая проводится бесплатно для всех желающих."
                               }
                            ]
                            """))),
            @ApiResponse(responseCode = "406", description = "Pageable wrong sort params",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "PropertyReferenceException",
                               "errorMessage": "No property 'i' found for type 'News'; Did you mean 'id'",
                               "errorCode": "406 NOT_ACCEPTABLE"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                       "errorMessage": "409 CONFLICT",
                                       "violations": [
                                         {
                                           "fieldName": "text",
                                           "message": "must not be blank"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<List<NewsResponse>> findAllByMatchingTextParams(@ParameterObject NewsRequest newsRequest,
                                                                   @ParameterObject Pageable pageable);

    @Operation(summary = "Save new News.", tags = "News",
            requestBody = @RequestBody(description = "RequestBody for NewsRequest",
                    content = @Content(schema = @Schema(implementation = NewsRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "title": "Привет из Беларуси",
                                      "text": "Беларусь - великая страна!"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class), examples = @ExampleObject("""
                            {
                               "id": 21,
                               "time": "2023-05-28T15:00:37",
                               "title": "Привет из Беларуси",
                               "text": "Беларусь - великая страна!"
                             }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                       "errorMessage": "409 CONFLICT",
                                       "violations": [
                                         {
                                           "fieldName": "text",
                                           "message": "must not be blank"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<NewsResponse> save(@Valid NewsRequest newsRequest,
                                      @Pattern(regexp = "^Bearer\\s.*$",
                                              message = "Header must starts with 'Bearer ' !") String token);

    @Operation(summary = "Update News by id.", tags = "News",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "21"),
            requestBody = @RequestBody(description = "RequestBody for NewsUpdateRequest",
                    content = @Content(schema = @Schema(implementation = NewsUpdateRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "title": "До свидания от Беларуси",
                                      "text": "Беларусь прощается с вами"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class), examples = @ExampleObject("""
                            {
                               "id": 21,
                               "time": "2023-05-28T15:03:28",
                               "title": "До свидания от Беларуси",
                               "text": "Беларусь прощается с вами"
                             }
                            """))),
            @ApiResponse(responseCode = "404", description = "No News with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchNewsException",
                               "errorMessage": "There is no News with ID 211 to update",
                               "errorCode": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                       "errorMessage": "409 CONFLICT",
                                       "violations": [
                                         {
                                           "fieldName": "title",
                                           "message": "must not be blank"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<NewsResponse> updateById(@Positive Long id,
                                            @Valid NewsUpdateRequest newsUpdateRequest);

    @Operation(summary = "Delete News by id with related Comments.", tags = "News",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "21"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                               "message": "News with ID 21 was successfully deleted"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Not News with this id to delete",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchNewsException",
                               "errorMessage": "There is no News with ID 222 to delete",
                               "errorCode": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                       "errorMessage": "409 CONFLICT",
                                       "violations": [
                                         {
                                           "fieldName": "deleteById.id",
                                           "message": "must be greater than 0"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<DeleteResponse> deleteById(@Positive Long id);

}
