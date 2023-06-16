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
import org.springframework.http.ResponseEntity;
import ru.clevertec.exceptionhandlerstarter.model.IncorrectData;
import ru.clevertec.newsservice.dto.proto.DeleteResponse;
import ru.clevertec.newsservice.dto.proto.UserAuthenticationRequest;
import ru.clevertec.newsservice.dto.proto.UserRegisterRequest;
import ru.clevertec.newsservice.dto.proto.UserUpdateRequest;
import ru.clevertec.newsservice.dto.proto.UserResponse;

@Tag(name = "Authentication", description = "The Authentication Api")
public interface AuthenticationOpenApi {

    @Operation(summary = "Register, save new User and get jwt token.", tags = "Authentication",
            requestBody = @RequestBody(description = "RequestBody for UserRegisterRequest",
                    content = @Content(schema = @Schema(implementation = UserRegisterRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "firstname": "Pavel",
                                      "lastname": "Shishkin",
                                      "email": "Green@mail.com",
                                      "password": "1234",
                                      "role": "ADMIN"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject("""
                            {
                              "id": 4,
                              "firstname": "Pavel",
                              "lastname": "Shishkin",
                              "email": "Green@mail.com",
                              "role": "ADMIN",
                              "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiQURNSU4ifV0sInN1YiI6IkdyZWVuQG1haWwuY29tIiwiaWF0IjoxNjg2MzExNDIxLCJleHAiOjE2ODYzOTc4MjF9.CPPgPruJXiABRI2466pT8jX62L9t9BTV00WuC_AHrNA",
                              "token_expiration": "Sat Jun 10 14:50:21 MSK 2023",
                              "created_time": "2023-06-09T14:50:21",
                              "updated_time": "2023-06-09T14:50:21"
                            }
                            """))),
            @ApiResponse(responseCode = "406", description = "Email is unique for User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "UniqueEmailException",
                              "error_message": "Email Green@mail.com is occupied! Another user is already registered by this email!",
                              "error_code": "406 NOT_ACCEPTABLE"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class),
                            examples = @ExampleObject("""
                                    {
                                      "exception": "ProtoValidationException",
                                      "error_message": ".UserRegisterRequest.role: must match pattern ADMIN|JOURNALIST|SUBSCRIBER - Got \\"SUPERMAN\\"",
                                      "error_code": "409 CONFLICT"
                                    }
                                    """)))
    })
    ResponseEntity<UserResponse> register(UserRegisterRequest request);

    @Operation(summary = "Authenticate User and get jwt token.", tags = "Authentication",
            requestBody = @RequestBody(description = "RequestBody for UserAuthenticationRequest",
                    content = @Content(schema = @Schema(implementation = UserAuthenticationRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "email": "ChakcNunChuck@gmail.com",
                                      "password": "555"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject("""
                            {
                              "id": 1,
                              "firstname": "Chuck",
                              "lastname": "Norris",
                              "email": "ChakcNunChuck@gmail.com",
                              "role": "SUBSCRIBER",
                              "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiU1VCU0NSSUJFUiJ9XSwic3ViIjoiQ2hha2NOdW5DaHVja0BnbWFpbC5jb20iLCJpYXQiOjE2ODY5MTUzNjcsImV4cCI6MTY4NzAwMTc2N30.zTK3gwck5_SSORSufAfbON8UO4cOcFe4-xJTAFS9dsc",
                              "token_expiration": "Sat Jun 17 14:36:07 MSK 2023",
                              "created_time": "2023-06-06T16:45:59",
                              "updated_time": "2023-06-06T16:45:59"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Wrong password for User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "UserApiClientException",
                              "error_message": "Bad credentials",
                              "error_code": "401 UNAUTHORIZED"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No User with this email in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchUserEmailException",
                              "error_message": "User with email ChakcNunChuck@1gmail.com is not exist",
                              "error_code": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class),
                            examples = @ExampleObject("""
                                    {
                                      "exception": "ProtoValidationException",
                                      "error_message": ".UserAuthenticationRequest.email: should be a valid email - Got \\"ChakcNunChuckgmail.com\\"",
                                      "error_code": "409 CONFLICT"
                                    }
                                    """)))
    })
    ResponseEntity<UserResponse> authenticate(UserAuthenticationRequest request);

    @Operation(summary = "Update User by token.", tags = "Authentication",
            security = @SecurityRequirement(name = "Bearer Authentication"),
            requestBody = @RequestBody(description = "RequestBody for UserUpdateRequest",
                    content = @Content(schema = @Schema(implementation = UserUpdateRequest.class),
                            examples = @ExampleObject("""
                                    {
                                      "firstname": "Igor",
                                      "lastname": "Zavadskiy",
                                      "password": "987"
                                    }
                                    """))))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponse.class), examples = @ExampleObject("""
                            {
                              "id": 2,
                              "firstname": "Igor",
                              "lastname": "Zavadskiy",
                              "email": "Shwarsz@yahoo.com",
                              "role": "JOURNALIST",
                              "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiSk9VUk5BTElTVCJ9XSwic3ViIjoiU2h3YXJzekB5YWhvby5jb20iLCJpYXQiOjE2ODYzMTA0NTUsImV4cCI6MTY4NjM5Njg1NX0.3xmnKRAPcAjPNo_kN8myvlOfyGaweGgjt9DFhfa8LuQ",
                              "token_expiration": "Sat Jun 10 14:34:15 MSK 2023",
                              "created_time": "2023-06-06T12:33:47",
                              "updated_time": "2023-06-09T15:05:50"
                            }
                            """))),
            @ApiResponse(responseCode = "401", description = "Not Authenticated User",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "UserApiClientException",
                              "error_message": "Bad credentials",
                              "error_code": "401 UNAUTHORIZED"
                            }
                            """))),
            @ApiResponse(responseCode = "404", description = "No User with this email in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchUserEmailException",
                              "error_message": "User with email ChakcNunChuck@1gmail.com is not exist",
                              "error_code": "404 NOT_FOUND"
                            }
                            """))),
            @ApiResponse(responseCode = "409", description = "Validation error",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class),
                            examples = @ExampleObject("""
                                    {
                                      "exception": "ProtoValidationException",
                                      "error_message": ".UserUpdateRequest.firstname: must match pattern ^[a-zA-Zа-яА-ЯёЁ]+$ - Got \\"Igor1\\"",
                                      "error_code": "409 CONFLICT"
                                    }
                                    """)))
    })
    ResponseEntity<UserResponse> updateByToken(UserUpdateRequest request,
                                               @Parameter(hidden = true) String token);

    @Operation(summary = "Delete User by token.", tags = "Authentication",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = DeleteResponse.class), examples = @ExampleObject("""
                            {
                              "message": "User with email Shwarsz@yahoo.com was successfully deleted"
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
            @ApiResponse(responseCode = "404", description = "No User with this email in database",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncorrectData.class), examples = @ExampleObject("""
                            {
                              "exception": "NoSuchUserEmailException",
                              "error_message": "User with email ChakcNunChuck@1gmail.com is not exist",
                              "error_code": "404 NOT_FOUND"
                            }
                            """)))
    })
    ResponseEntity<DeleteResponse> deleteByToken(@Parameter(hidden = true) String token);

}
