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
import jakarta.validation.constraints.Positive;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.exceptionhandlerstarter.model.IncorrectData;
import ru.clevertec.exceptionhandlerstarter.model.ValidationErrorResponse;
import ru.clevertec.newsservice.dto.proto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.CommentResponse;
import ru.clevertec.newsservice.dto.proto.NewsWithCommentsResponse;
import ru.clevertec.newsservice.dto.proto.CommentRequest;
import ru.clevertec.newsservice.dto.proto.CommentResponseList;
import ru.clevertec.newsservice.dto.proto.CommentWithNewsRequest;

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
                              "time": "2023-06-14T10:35:00",
                              "text": "This is so cool! I wish I could see it in person!",
                              "username": "VolcanoFan",
                              "email": "volcanofan@gmail.com"
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
                              "time": "2023-06-14T12:00:00",
                              "title": "New song by Ed Sheeran tops charts",
                              "text": "The new song by the popular singer-songwriter Ed Sheeran, titled Perfect Harmony, has topped the charts in several countries around the world. The song, which is a duet with his wife Cherry Seaborn, is a romantic ballad that expresses their love and happiness. The song has been praised for its melody, lyrics, and vocals.",
                              "email": "music@news.com",
                              "comments": [
                                {
                                  "id": 92,
                                  "time": "2023-06-14T12:02:00",
                                  "text": "This is so cool! The song is so beautiful!",
                                  "username": "SheeranLover",
                                  "email": "sheeranlover@yahoo.com"
                                },
                                {
                                  "id": 143,
                                  "time": "2023-06-14T12:03:00",
                                  "text": "This is so lame. Ed Sheeran is boring.",
                                  "username": "SheeranHater2",
                                  "email": "sheeranhater2@outlook.com"
                                },
                                {
                                  "id": 93,
                                  "time": "2023-06-14T12:03:00",
                                  "text": "This is so lame. Ed Sheeran is boring.",
                                  "username": "SheeranHater",
                                  "email": "sheeranhater@outlook.com"
                                },
                                {
                                  "id": 141,
                                  "time": "2023-06-14T12:01:00",
                                  "text": "Wow! That's awesome! I love Ed Sheeran!",
                                  "username": "SheeranFan2",
                                  "email": "sheeranfan2@gmail.com"
                                },
                                {
                                  "id": 91,
                                  "time": "2023-06-14T12:01:00",
                                  "text": "Wow! That's awesome! I love Ed Sheeran!",
                                  "username": "SheeranFan",
                                  "email": "sheeranfan@gmail.com"
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
            @Parameter(name = "text", description = "Enter your text here", example = "This"),
            @Parameter(name = "username", description = "Enter your username here", example = "dinolover"),
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
                                "id": 37,
                                "time": "2023-06-14T11:07:00",
                                "text": "This is so cool! I wonder what it looked like.",
                                "username": "DinoLover",
                                "email": "dinolover@yahoo.com"
                              },
                              {
                                "id": 162,
                                "time": "2023-06-14T11:07:00",
                                "text": "This is so cool! I wonder what it looked like.",
                                "username": "DinoLover2",
                                "email": "dinolover2@yahoo.com"
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
    ResponseEntity<CommentResponseList> findAllByMatchingTextParams(String text,
                                                                    String username,
                                                                    @ParameterObject Pageable pageable);

    @Operation(summary = "Save new Comment and related it with News by newsId.", tags = "Comment",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @RequestBody(description = "RequestBody for CommentWithNewsRequest",
                    content = @Content(schema = @Schema(implementation = CommentWithNewsRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "text": "Not bad at all :(",
                                      "username": "Vasiliy",
                                      "news_id": 20
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class), examples = @ExampleObject("""
                            {
                              "id": 203,
                              "time": "2023-06-16T14:31:34",
                              "text": "Not bad at all :(",
                              "username": "Vasiliy",
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
                            schema = @Schema(implementation = IncorrectData.class),
                            examples = @ExampleObject("""
                                    {
                                      "exception": "ProtoValidationException",
                                      "error_message": ".CommentWithNewsRequest.news_id: must be greater than 0 - Got 0",
                                      "error_code": "409 CONFLICT"
                                    }
                                    """)))
    })
    ResponseEntity<CommentResponse> save(CommentWithNewsRequest commentWithNewsRequest,
                                         @Parameter(hidden = true) String token);

    @Operation(summary = "Update Comment by id.", tags = "Comment",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter id here", example = "191"),
            requestBody = @RequestBody(description = "RequestBody for CommentWithNewsRequest",
                    content = @Content(schema = @Schema(implementation = CommentRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "text": "Wow, that's cool!",
                                      "username": "Svetlana"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommentResponse.class), examples = @ExampleObject("""
                            {
                              "id": 191,
                              "time": "2023-06-16T14:32:55",
                              "text": "Wow, that's cool!",
                              "username": "Svetlana",
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
                                          "field_name": "updateById.id",
                                          "error_message": "must be greater than 0"
                                        }
                                      ]
                                    }
                                    """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class),
                            examples = @ExampleObject("""
                                    {
                                      "exception": "ProtoValidationException",
                                      "error_message": ".CommentRequest.username: must match pattern ^[a-zA-Zа-яА-ЯёЁ0-9@_-]+$ - Got \\"Svetlana111   \\"",
                                      "error_code": "409 CONFLICT"
                                    }
                                    """)))
    })
    ResponseEntity<CommentResponse> updateById(@Positive Long id,
                                               CommentRequest commentRequest,
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
