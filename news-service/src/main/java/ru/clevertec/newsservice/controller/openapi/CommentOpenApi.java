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
import ru.clevertec.exceptionhandlerstarter.model.IncorrectData;
import ru.clevertec.exceptionhandlerstarter.model.ValidationErrorResponse;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.comment.CommentRequest;
import ru.clevertec.newsservice.dto.comment.CommentResponse;
import ru.clevertec.newsservice.dto.comment.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.news.NewsWithCommentsResponse;

import java.util.List;

@Validated
@Tag(name = "Comment", description = "The Comment Api")
public interface CommentOpenApi {

    @Operation(summary = "Find Comment by id.", tags = "Comment",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "5"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class), examples = @ExampleObject("""
                            {
                              "id": 5,
                              "time": "2023-06-03T17:56:12",
                              "text": "Все это бессмысленно, никто не будет использовать это",
                              "username": "Дмитрий"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Comment with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchCommentException",
                              "errorMessage": "Comment with ID 233 does not exist",
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
    ResponseEntity<CommentResponse> findById(@Positive Long id);

    @Operation(summary = "Find News by newsId with its Comments with pagination.", tags = "Comment", parameters = {
            @Parameter(name = "newsId", description = "Enter News id here", example = "19"),
            @Parameter(name = "page", description = "Enter your page number here", example = "1"),
            @Parameter(name = "size", description = "Enter your page size here", example = "5"),
            @Parameter(name = "sort", description = "Enter your sort by(id, time, text or username) here",
                    schema = @Schema(type = "array", example = "[\"username,desc\"]"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News with Comments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsWithCommentsResponse.class), examples = @ExampleObject("""
                            {
                               "id": 19,
                               "time": "2023-05-27T10:55:59",
                               "title": "В Беларуси прошли массовые акции протеста против алкоголизма",
                               "text": "В Беларуси прошли массовые акции протеста против алкоголизма, который удерживает власть над алкоголиками более 40 лет.",
                               "comments": [
                                 {
                                   "id": 187,
                                   "time": "2023-06-18T15:23:34",
                                   "text": "Не согласен с автором, это не сработает",
                                   "username": "Кира"
                                 },
                                 {
                                   "id": 183,
                                   "time": "2023-06-16T12:45:34",
                                   "text": "Мне кажется, что это не совсем точно",
                                   "username": "Ирина"
                                 },
                                 {
                                   "id": 185,
                                   "time": "2023-06-17T11:34:56",
                                   "text": "Спасибо за информацию, очень интересно",
                                   "username": "Егор"
                                 },
                                 {
                                   "id": 182,
                                   "time": "2023-06-16T09:34:12",
                                   "text": "Не очень понятно, надо бы больше примеров",
                                   "username": "Василий"
                                 },
                                 {
                                   "id": 190,
                                   "time": "2023-06-20T11:12:45",
                                   "text": "Мне кажется, что это не совсем правильно",
                                   "username": "Антон"
                                 }
                               ]
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No News with this newsId in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchNewsException",
                              "errorMessage": "News with ID 22 does not exist",
                              "errorCode": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "406", description = "Pageable wrong sort params",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "PropertyReferenceException",
                              "errorMessage": "No property 'usename' found for type 'Comment'; Did you mean 'username'",
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
                                           "fieldName": "findNewsByNewsIdWithComments.newsId",
                                           "message": "must be greater than 0"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<NewsWithCommentsResponse> findNewsByNewsIdWithComments(@Positive Long newsId,
                                                                          @ParameterObject Pageable pageable);

    @Operation(summary = "Find all Comments by matching text and username params with pagination.",
            tags = "Comment", parameters = {
            @Parameter(name = "username", description = "Enter your username here", example = "Анна"),
            @Parameter(name = "text", description = "Enter your text here", example = "Какая"),
            @Parameter(name = "page", description = "Enter your page number here", example = "0"),
            @Parameter(name = "size", description = "Enter your page size here", example = "10"),
            @Parameter(name = "sort", description = "Enter your sort by(id, time, text or username) here",
                    schema = @Schema(type = "array", example = "[\"username\"]"))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of Comments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class), examples = @ExampleObject("""
                            [
                               {
                                 "id": 3,
                                 "time": "2023-06-02T09:12:08",
                                 "text": "Какая интересная статья, мне очень понравилось",
                                 "username": "Анна"
                               },
                               {
                                 "id": 48,
                                 "time": "2023-06-02T09:12:08",
                                 "text": "Какая интересная статья, мне очень понравилось",
                                 "username": "Анна"
                               },
                               {
                                 "id": 93,
                                 "time": "2023-06-02T09:12:08",
                                 "text": "Какая интересная статья, мне очень понравилось",
                                 "username": "Анна"
                               },
                               {
                                 "id": 138,
                                 "time": "2023-06-02T09:12:08",
                                 "text": "Какая интересная статья, мне очень понравилось",
                                 "username": "Анна"
                               },
                               {
                                 "id": 199,
                                 "time": "2023-06-02T09:12:08",
                                 "text": "Какая интересная статья, мне очень понравилось",
                                 "username": "Анна"
                               }
                            ]
                            """))),
            @ApiResponse(responseCode = "406", description = "Pageable wrong sort params",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "PropertyReferenceException",
                               "errorMessage": "No property 'tex' found for type 'Comment'; Did you mean 'text'",
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
                                          "fieldName": "username",
                                          "message": "This field must contain only letters of the Russian and English alphabets, numbers, symbols(@ _ -) without spaces in any case"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<List<CommentResponse>> findAllByMatchingTextParams(@ParameterObject CommentRequest commentRequest,
                                                                      @ParameterObject Pageable pageable);

    @Operation(summary = "Save new Comment and related it with News by newsId.", tags = "Comment",
            requestBody = @RequestBody(description = "RequestBody for CommentWithNewsRequest",
                    content = @Content(schema = @Schema(implementation = CommentWithNewsRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "text": "Как то не очень :(",
                                      "username": "Василий",
                                      "news_id": 20
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class), examples = @ExampleObject("""
                            {
                               "id": 204,
                               "time": "2023-05-28T14:19:41",
                               "text": "Как то не очень :(",
                               "username": "Василий"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No News with this newsId in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchNewsException",
                               "errorMessage": "News with ID 22 does not exist",
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
                                           "fieldName": "text",
                                           "message": "size must be between 3 and 500"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<CommentResponse> save(@Valid CommentWithNewsRequest commentWithNewsRequest,
                                         @Pattern(regexp = "^Bearer\\s.*$",
                                                 message = "Header must starts with 'Bearer ' !") String token);

    @Operation(summary = "Update Comment by id.", tags = "Comment",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "191"),
            requestBody = @RequestBody(description = "RequestBody for CommentWithNewsRequest",
                    content = @Content(schema = @Schema(implementation = CommentRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "text": "Воу, это круто!",
                                      "username": "Светка"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class), examples = @ExampleObject("""
                            {
                               "id": 190,
                               "time": "2023-05-28T14:26:23",
                               "text": "Воу, это круто!",
                               "username": "Светка"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Comment with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchCommentException",
                               "errorMessage": "There is no Comment with ID 333 to update",
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
                                           "fieldName": "text",
                                           "message": "size must be between 3 and 500"
                                         }
                                       ]
                                    }
                                    """)))
    })
    ResponseEntity<CommentResponse> updateById(@Positive Long id,
                                               @Valid CommentRequest commentRequest,
                                               @Pattern(regexp = "^Bearer\\s.*$",
                                                       message = "Header must starts with 'Bearer ' !") String token);

    @Operation(summary = "Delete Comment by id.", tags = "Comment",
            parameters = @Parameter(name = "id", description = "Enter id here", example = "191"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                              "message": "Comment with ID 191 was successfully deleted"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Not Tag with this id to delete",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchCommentException",
                               "errorMessage": "There is no Comment with ID 456 to delete",
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
    ResponseEntity<DeleteResponse> deleteById(@Positive Long id,
                                              @Pattern(regexp = "^Bearer\\s.*$",
                                                      message = "Header must starts with 'Bearer ' !") String token);

}
