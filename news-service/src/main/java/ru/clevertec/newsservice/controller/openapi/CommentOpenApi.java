package ru.clevertec.newsservice.controller.openapi;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
                              "username": "Дмитрий",
                              "email": "cthompson05@gmail.com"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Comment with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchCommentException",
                              "error_message": "Comment with ID 233 does not exist",
                              "error_code": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "error_code": "409 CONFLICT",
                                      "violations": [
                                        {
                                          "field_name": "findById.id",
                                          "error_message": "must be greater than 0"
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
                              "email": "rrogers19@hotmail.com",
                              "comments": [
                                {
                                  "id": 187,
                                  "time": "2023-06-18T15:23:34",
                                  "text": "Не согласен с автором, это не сработает",
                                  "username": "Кира",
                                  "email": "jhernandez37@gmail.com"
                                },
                                {
                                  "id": 183,
                                  "time": "2023-06-16T12:45:34",
                                  "text": "Мне кажется, что это не совсем точно",
                                  "username": "Ирина",
                                  "email": "flopez33@gmail.com"
                                },
                                {
                                  "id": 185,
                                  "time": "2023-06-17T11:34:56",
                                  "text": "Спасибо за информацию, очень интересно",
                                  "username": "Егор",
                                  "email": "hjohnson35@hotmail.com"
                                },
                                {
                                  "id": 182,
                                  "time": "2023-06-16T09:34:12",
                                  "text": "Не очень понятно, надо бы больше примеров",
                                  "username": "Василий",
                                  "email": "ejames32@outlook.com"
                                },
                                {
                                  "id": 190,
                                  "time": "2023-06-20T11:12:45",
                                  "text": "Мне кажется, что это не совсем правильно",
                                  "username": "Антон",
                                  "email": "msmith40@outlook.com"
                                }
                              ]
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No News with this newsId in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchNewsException",
                              "error_message": "News with ID 22 does not exist",
                              "error_code": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "406", description = "Pageable wrong sort params",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "PropertyReferenceException",
                              "error_message": "No property 'usename' found for type 'Comment'; Did you mean 'username'",
                              "error_code": "406 NOT_ACCEPTABLE"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                       "error_code": "409 CONFLICT",
                                       "violations": [
                                         {
                                           "field_name": "findNewsByNewsIdWithComments.newsId",
                                           "error_message": "must be greater than 0"
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
                                "username": "Анна",
                                "email": "klee03@hotmail.com"
                              },
                              {
                                "id": 48,
                                "time": "2023-06-02T09:12:08",
                                "text": "Какая интересная статья, мне очень понравилось",
                                "username": "Анна",
                                "email": "ucook48@outlook.com"
                              },
                              {
                                "id": 93,
                                "time": "2023-06-02T09:12:08",
                                "text": "Какая интересная статья, мне очень понравилось",
                                "username": "Анна",
                                "email": "pscott43@hotmail.com"
                              },
                              {
                                "id": 138,
                                "time": "2023-06-02T09:12:08",
                                "text": "Какая интересная статья, мне очень понравилось",
                                "username": "Анна",
                                "email": "krivera38@yahoo.com"
                              },
                              {
                                "id": 199,
                                "time": "2023-06-02T09:12:08",
                                "text": "Какая интересная статья, мне очень понравилось",
                                "username": "Анна",
                                "email": "vevans49@gmail.com"
                              }
                            ]
                            """))),
            @ApiResponse(responseCode = "406", description = "Pageable wrong sort params",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "PropertyReferenceException",
                              "error_message": "No property 'tex' found for type 'Comment'; Did you mean 'text'",
                              "error_code": "406 NOT_ACCEPTABLE"
                            }
                            """)))
    })
    ResponseEntity<List<CommentResponse>> findAllByMatchingTextParams(@ParameterObject CommentRequest commentRequest,
                                                                      @ParameterObject Pageable pageable);

    @Operation(summary = "Save new Comment and related it with News by newsId.", tags = "Comment",
            security = @SecurityRequirement(name = "Bearer Authentication"),
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
                              "id": 207,
                              "time": "2023-06-08T22:34:43",
                              "text": "Как то не очень :(",
                              "username": "Василий",
                              "email": "Green@mail.com"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "InsufficientAuthenticationException",
                              "error_message": "Full authentication is required to access this resource",
                              "error_code": "401 UNAUTHORIZED"
                            }
                            """))),
            @ApiResponse(responseCode = "403", description = "Access denied for User with this role",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "AccessDeniedForThisRoleException",
                              "error_message": "Access Denied for role: JOURNALIST",
                              "error_code": "403 FORBIDDEN"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No News with this newsId in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchNewsException",
                              "error_message": "News with ID 22 does not exist",
                              "error_code": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "error_code": "409 CONFLICT",
                                      "violations": [
                                        {
                                          "field_name": "text",
                                          "error_message": "size must be between 3 and 500"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<CommentResponse> save(@Valid CommentWithNewsRequest commentWithNewsRequest,
                                         @Parameter(hidden = true) String token);

    @Operation(summary = "Update Comment by id.", tags = "Comment",
            security = @SecurityRequirement(name = "Bearer Authentication"),
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
                              "id": 208,
                              "time": "2023-06-08T22:36:44",
                              "text": "Воу, это круто!",
                              "username": "Светка",
                              "email": "ChakcNunChuck@gmail.com"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "InsufficientAuthenticationException",
                              "error_message": "Full authentication is required to access this resource",
                              "error_code": "401 UNAUTHORIZED"
                            }
                            """))),
            @ApiResponse(responseCode = "403", description = "Access denied for User with this role",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "AccessDeniedForThisRoleException",
                              "error_message": "Access Denied for role: JOURNALIST",
                              "error_code": "403 FORBIDDEN"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No Comment with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchCommentException",
                              "error_message": "There is no Comment with ID 333 to update",
                              "error_code": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "405", description = "User with this role does not have required permission",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "UserDoesNotHavePermissionException",
                              "error_message": "With role SUBSCRIBER you can update or delete only your own news/comments",
                              "error_code": "405 METHOD_NOT_ALLOWED"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "error_code": "409 CONFLICT",
                                      "violations": [
                                        {
                                          "field_name": "text",
                                          "error_message": "size must be between 3 and 500"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<CommentResponse> updateById(@Positive Long id,
                                               @Valid CommentRequest commentRequest,
                                               @Parameter(hidden = true) String token);

    @Operation(summary = "Delete Comment by id.", tags = "Comment",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter id here", example = "191"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                              "message": "Comment with ID 191 was successfully deleted"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "InsufficientAuthenticationException",
                              "error_message": "Full authentication is required to access this resource",
                              "error_code": "401 UNAUTHORIZED"
                            }
                            """))),
            @ApiResponse(responseCode = "403", description = "Access denied for User with this role",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "AccessDeniedForThisRoleException",
                              "error_message": "Access Denied for role: JOURNALIST",
                              "error_code": "403 FORBIDDEN"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Not Tag with this id to delete",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchCommentException",
                              "error_message": "There is no Comment with ID 456 to delete",
                              "error_code": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "405", description = "User with this role does not have required permission",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "UserDoesNotHavePermissionException",
                              "error_message": "With role SUBSCRIBER you can update or delete only your own news/comments",
                              "error_code": "405 METHOD_NOT_ALLOWED"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class),
                            examples = @ExampleObject("""
                                    {
                                      "error_code": "409 CONFLICT",
                                      "violations": [
                                        {
                                          "field_name": "deleteById.id",
                                          "error_message": "must be greater than 0"
                                        }
                                      ]
                                    }
                                    """)))
    })
    ResponseEntity<DeleteResponse> deleteById(@Positive Long id,
                                              @Parameter(hidden = true) String token);

}
