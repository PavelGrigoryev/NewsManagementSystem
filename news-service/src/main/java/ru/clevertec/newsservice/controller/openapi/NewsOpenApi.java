package ru.clevertec.newsservice.controller.openapi;

import com.google.protobuf.InvalidProtocolBufferException;
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
import ru.clevertec.newsservice.dto.proto.NewsResponse;
import ru.clevertec.newsservice.dto.proto.NewsRequest;
import ru.clevertec.newsservice.dto.proto.NewsResponseList;

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
                              "time": "2023-06-14T12:00:00",
                              "title": "New song by Ed Sheeran tops charts",
                              "text": "The new song by the popular singer-songwriter Ed Sheeran, titled Perfect Harmony, has topped the charts in several countries around the world. The song, which is a duet with his wife Cherry Seaborn, is a romantic ballad that expresses their love and happiness. The song has been praised for its melody, lyrics, and vocals.",
                              "email": "music@news.com"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No News with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchNewsException",
                              "error_message": "News with ID 122 does not exist",
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
                                "time": "2023-06-14T10:55:00",
                                "title": "New album by Adele breaks records",
                                "text": "The British singer-songwriter Adele has released her new album, titled 33, which has broken records for the most streams and sales in the first week. The album, which is her first since 2015, features 12 songs that explore themes of love, loss, and healing. The album has received critical acclaim and praise from fans and celebrities alike.",
                                "email": "music@news.com"
                              },
                              {
                                "id": 7,
                                "time": "2023-06-14T11:00:00",
                                "title": "Bitcoin reaches new all-time high",
                                "text": "The cryptocurrency Bitcoin has reached a new all-time high of $100,000 USD per coin, surpassing its previous record of $64,000 USD in April 2021. The surge in price is attributed to increased adoption and demand from institutional investors, as well as the launch of the first Bitcoin exchange-traded fund (ETF) in the US. Bitcoin is the most popular and valuable cryptocurrency in the world, with a market capitalization of over $1.8 trillion USD.",
                                "email": "finance@news.com"
                              },
                              {
                                "id": 8,
                                "time": "2023-06-14T11:05:00",
                                "title": "New dinosaur species discovered in Argentina",
                                "text": "A team of paleontologists from Argentina and Brazil have discovered a new species of dinosaur that lived about 90 million years ago. The dinosaur, named Argentinosaurus giganteus, is estimated to be the largest land animal ever to exist, measuring about 40 meters long and weighing about 100 tons. The dinosaur belongs to the group of sauropods, which are long-necked herbivorous dinosaurs.",
                                "email": "science@news.com"
                              },
                              {
                                "id": 9,
                                "time": "2023-06-14T11:10:00",
                                "title": "New York City hit by massive blackout",
                                "text": "A massive blackout has hit New York City, leaving millions of people without power and disrupting transportation and communication systems. The blackout was caused by a failure in the transmission grid that supplies electricity to the city. The authorities are working to restore power as soon as possible and to ensure public safety and order.",
                                "email": "city@news.com"
                              },
                              {
                                "id": 10,
                                "time": "2023-06-14T11:15:00",
                                "title": "Oprah Winfrey announces retirement from TV",
                                "text": "The legendary talk show host and media mogul Oprah Winfrey has announced that she will retire from TV after 40 years of career. Winfrey said that she will focus on her philanthropic and educational projects, as well as her own network, OWN. Winfrey is widely regarded as one of the most influential and inspiring women in the world, having interviewed countless celebrities, politicians, and ordinary people on her show.",
                                "email": "entertainment@news.com"
                              }
                            ]
                            """))),
            @ApiResponse(responseCode = "406", description = "Pageable wrong sort params",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "PropertyReferenceException",
                               "error_message": "No property 'tit' found for type 'News'; Did you mean 'id','text','time','title'",
                               "error_code": "406 NOT_ACCEPTABLE"
                            }
                            """))),
    })
    ResponseEntity<NewsResponseList> findAll(@ParameterObject Pageable pageable) throws InvalidProtocolBufferException;

    @Operation(summary = "Find all News by matching text and title params with pagination.",
            tags = "News", parameters = {
            @Parameter(name = "title", description = "Enter your title here", example = "New s"),
            @Parameter(name = "text", description = "Enter your text here", example = "The"),
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
                                "id": 20,
                                "time": "2023-06-14T12:05:00",
                                "title": "New scandal by Donald Trump shocks nation",
                                "text": "The new scandal by the former president Donald Trump, involving his alleged involvement in a bribery scheme with a foreign leader, has shocked the nation and sparked outrage. The scandal, which was revealed by a whistleblower who leaked a phone call transcript between Trump and the leader of Ukraine, shows that Trump asked the leader to investigate his political rival Joe Biden in exchange for military aid. The scandal has led to calls for impeachment and criminal charges against Trump.",
                                "email": "politics@news.com"
                              },
                              {
                                "id": 19,
                                "time": "2023-06-14T12:00:00",
                                "title": "New song by Ed Sheeran tops charts",
                                "text": "The new song by the popular singer-songwriter Ed Sheeran, titled Perfect Harmony, has topped the charts in several countries around the world. The song, which is a duet with his wife Cherry Seaborn, is a romantic ballad that expresses their love and happiness. The song has been praised for its melody, lyrics, and vocals.",
                                "email": "music@news.com"
                              }
                            ]
                            """))),
            @ApiResponse(responseCode = "406", description = "Pageable wrong sort params",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "PropertyReferenceException",
                               "error_message": "No property 'i' found for type 'News'; Did you mean 'id'",
                               "error_code": "406 NOT_ACCEPTABLE"
                            }
                            """)))
    })
    ResponseEntity<NewsResponseList> findAllByMatchingTextParams(String title,
                                                                 String text,
                                                                 @ParameterObject Pageable pageable);

    @Operation(summary = "Save new News.", tags = "News",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @RequestBody(description = "RequestBody for NewsRequest",
                    content = @Content(schema = @Schema(implementation = NewsRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "title": "Hello from Belarus",
                                      "text": "Belarus is a great country!"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News saved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class), examples = @ExampleObject("""
                            {
                              "id": 21,
                              "time": "2023-06-16T14:15:57",
                              "title": "Hello from Belarus",
                              "text": "Belarus is a great country!",
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
                              "error_message": "Access Denied for role: SUBSCRIBER",
                              "error_code": "403 FORBIDDEN"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class),
                            examples = @ExampleObject("""
                                    {
                                      "exception": "ProtoValidationException",
                                      "error_message": ".NewsRequest.text: length must be at least 3 but got: 2 - Got \\"Be\\"",
                                      "error_code": "409 CONFLICT"
                                    }
                                    """)))
    })
    ResponseEntity<NewsResponse> save(NewsRequest newsRequest,
                                      @Parameter(hidden = true) String token);

    @Operation(summary = "Update News by id.", tags = "News",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter id here", example = "21"),
            requestBody = @RequestBody(description = "RequestBody for NewsRequest",
                    content = @Content(schema = @Schema(implementation = NewsRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "title": "Good-bye from Belarus",
                                      "text": "Belarus says goodbye to you!"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "News updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewsResponse.class), examples = @ExampleObject("""
                            {
                              "id": 21,
                              "time": "2023-06-16T14:23:06",
                              "title": "Good-bye from Belarus",
                              "text": "Belarus says goodbye to you!",
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
                              "error_message": "Access Denied for role: SUBSCRIBER",
                              "error_code": "403 FORBIDDEN"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No News with this id in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                               "exception": "NoSuchNewsException",
                               "error_message": "There is no News with ID 211 to update",
                               "error_code": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "405", description = "User with this role does not have required permission",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "UserDoesNotHavePermissionException",
                              "error_message": "With role JOURNALIST you can update or delete only your own news/comments",
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
                                      "error_message": ".NewsRequest.text: length must be at least 3 but got: 2 - Got \\"Be\\"",
                                      "error_code": "409 CONFLICT"
                                    }
                                    """)))
    })
    ResponseEntity<NewsResponse> updateById(@Positive Long id,
                                            NewsRequest newsRequest,
                                            @Parameter(hidden = true) String token);

    @Operation(summary = "Delete News by id with related Comments.", tags = "News",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            parameters = @Parameter(name = "id", description = "Enter id here", example = "21"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "News deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                              "message": "News with ID 21 was successfully deleted"
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
                              "error_message": "Access Denied for role: SUBSCRIBER",
                              "error_code": "403 FORBIDDEN"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "Not News with this id to delete",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchNewsException",
                              "error_message": "There is no News with ID 222 to delete",
                              "error_code": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "405", description = "User with this role does not have required permission",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "UserDoesNotHavePermissionException",
                              "error_message": "With role JOURNALIST you can update or delete only your own news/comments",
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
