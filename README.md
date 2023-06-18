# NewsManagementSystem application

RESTful web service that implements functionality for working with the news management system.
SpringCloudConfigServer located [here](https://github.com/PavelGrigoryev/SpringCloudConfigServer).

## Author: [Grigoryev Pavel](https://pavelgrigoryev.github.io/GrigoryevPavel/)

### Technologies that I used on the project:

* Java 17
* Gradle 8.0
* Lombok plugin 8.0.1
* Protobuf plugin 0.9.3
* Postgresql 14.5
* Spring-boot 3.1.0
* Spring-boot-starter-data-jpa
* Spring-boot-starter-data-redis
* Spring-boot-starter-web
* Spring-boot-starter-validation
* Spring-boot-starter-aop
* Spring-boot-configuration-processor
* Spring-boot-starter-security
* Spring-cloud-starter-openfeign
* Spring-cloud-config-server
* Spring-cloud-starter-config
* Springdoc-openapi-starter-webmvc-ui 2.1.0
* Jsonwebtoken-jjwt-api 0.11.5
* Jsonwebtoken-jjwt-impl 0.11.5
* Jsonwebtoken-jjwt-jackson 0.11.5
* Protobuf-java 3.23.2
* Protobuf-java-util 3.23.2
* Protoc-gen-validate-pgv-java-grpc 0.6.13
* Jackson-datatype-protobuf 0.9.13
* Liquibase
* Mapstruct 1.5.3.Final
* Spring-boot-starter-test
* Spring-security-test
* Spring-cloud-starter-contract-stub-runner
* Testcontainers-Postgresql 1.18.0

### Instructions to run application locally(dev profile):

1. You must have [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
   [Intellij IDEA Ultimate](https://www.jetbrains.com/idea/download/),
   [Postgresql](https://www.postgresql.org/download/) and [Redis](https://redis.io/) installed
   (P.S.: You can deploy postgresql and redis in a [DockerPostgres](https://hub.docker.com/_/postgres) and
   [DockerRedis](https://hub.docker.com/_/redis) container).
2. You need [Docker](https://www.docker.com/products/docker-desktop/) for integration tests with testcontainers.
3. In Postgresql you have to create two databases. Run script [db](db/db.sql).
4. In [news-service-application.yaml](news-service/src/main/resources/application.yaml) enter dev in line №5,
   And in [user-service-application.yaml](user-service/src/main/resources/application.yaml) enter dev in line №5.
5. Run [NewsServiceApplication.java](news-service/src/main/java/ru/clevertec/newsservice/NewsServiceApplication.java)
   And
   run [UserServiceApplication.java](user-service/src/main/java/ru/clevertec/userservice/UserServiceApplication.java).
   Liquibase will create the required tables and fill them with default values.
   Proto dtos and proto validators will generate automatically.
6. If you want to change something (example: cache algorithm, default page size, to turn off logs), go to
   [news-service-application-dev.yaml](news-service/src/main/resources/application-dev.yaml) and
   [user-service-application-dev.yaml](user-service/src/main/resources/application-dev.yaml)
7. Application is ready to work.

### Instruction to run application in Docker(prod profile):

1. You must have [Docker](https://www.docker.com/) installed.
2. Run `./gradlew build`.
3. Run line №2 in [docker-compose.yml](docker-compose.yaml).
4. You need your own setting for Spring Cloud Config Server in GitHub if you can't connect to mine,
   you can copy mine [ConfigServer](https://github.com/PavelGrigoryev/SpringCloudConfigServer),
   enter your url in line №6 for your
   GitHub: [application.yaml](spring-cloud-config/src/main/resources/application.yaml).
5. Application is ready to work.

### Unit tests

1. Generated proto files reduce the percentage of coverage in tests.
2. Tests have been written with 100% coverage of services and controllers. 92% coverage for all modules without proto
   files.
3. Integration tests for controllers and services with testcontainers are also written.
4. You can run the tests for this project, by at the root of the project executing:

```
./gradlew test
```

### Documentation

To view the API Swagger documentation, start the application and see:

* [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
* [http://localhost:8081/v3/api-docs](http://localhost:8081/v3/api-docs)
* [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Also, you can use mine http commands.

* [auth.http](news-service/src/main/resources/http/auth.http) for authentication api.
* [comments.http](news-service/src/main/resources/http/comments.http) for comment api.
* [news.http](news-service/src/main/resources/http/news.http) for news api.
* [env.json](news-service/src/main/resources/http/http-client.env.json) enter Jwt here.
* [users.http](user-service/src/main/resources/http/users.http) for user api(user-service without news-service).

## Functionalities

In summary the application can:

***

### NewsController

***

#### GET findById | Find News by id

* Request example findById [news.http](news-service/src/main/resources/http/news.http)
* Response examples:
* News retrieved successfully 200

```json
{
  "id": "19",
  "time": "2023-06-14T12:00:00",
  "title": "New song by Ed Sheeran tops charts",
  "text": "The new song by the popular singer-songwriter Ed Sheeran, titled Perfect Harmony, has topped the charts in several countries around the world. The song, which is a duet with his wife Cherry Seaborn, is a romantic ballad that expresses their love and happiness. The song has been praised for its melody, lyrics, and vocals.",
  "email": "music@news.com"
}
```

* No News with this id in database 404

```json
{
  "exception": "NoSuchNewsException",
  "error_message": "News with ID 122 does not exist",
  "error_code": "404 NOT_FOUND"
}
```

* Validation error 409

```.json
{
  "error_code": "409 CONFLICT",
  "violations": [
    {
      "field_name": "findById.id",
      "error_message": "must be greater than 0"
    }
  ]
}
```

#### GET findAll | Find all News with pagination

* Request example findAll [news.http](news-service/src/main/resources/http/news.http)
* Response examples:
* List of News retrieved successfully 200

```json
{
  "news": [
    {
      "id": "6",
      "time": "2023-06-14T10:55:00",
      "title": "New album by Adele breaks records",
      "text": "The British singer-songwriter Adele has released her new album, titled 33, which has broken records for the most streams and sales in the first week. The album, which is her first since 2015, features 12 songs that explore themes of love, loss, and healing. The album has received critical acclaim and praise from fans and celebrities alike.",
      "email": "music@news.com"
    },
    {
      "id": "7",
      "time": "2023-06-14T11:00:00",
      "title": "Bitcoin reaches new all-time high",
      "text": "The cryptocurrency Bitcoin has reached a new all-time high of $100,000 USD per coin, surpassing its previous record of $64,000 USD in April 2021. The surge in price is attributed to increased adoption and demand from institutional investors, as well as the launch of the first Bitcoin exchange-traded fund (ETF) in the US. Bitcoin is the most popular and valuable cryptocurrency in the world, with a market capitalization of over $1.8 trillion USD.",
      "email": "finance@news.com"
    },
    {
      "id": "8",
      "time": "2023-06-14T11:05:00",
      "title": "New dinosaur species discovered in Argentina",
      "text": "A team of paleontologists from Argentina and Brazil have discovered a new species of dinosaur that lived about 90 million years ago. The dinosaur, named Argentinosaurus giganteus, is estimated to be the largest land animal ever to exist, measuring about 40 meters long and weighing about 100 tons. The dinosaur belongs to the group of sauropods, which are long-necked herbivorous dinosaurs.",
      "email": "science@news.com"
    },
    {
      "id": "9",
      "time": "2023-06-14T11:10:00",
      "title": "New York City hit by massive blackout",
      "text": "A massive blackout has hit New York City, leaving millions of people without power and disrupting transportation and communication systems. The blackout was caused by a failure in the transmission grid that supplies electricity to the city. The authorities are working to restore power as soon as possible and to ensure public safety and order.",
      "email": "city@news.com"
    },
    {
      "id": "10",
      "time": "2023-06-14T11:15:00",
      "title": "Oprah Winfrey announces retirement from TV",
      "text": "The legendary talk show host and media mogul Oprah Winfrey has announced that she will retire from TV after 40 years of career. Winfrey said that she will focus on her philanthropic and educational projects, as well as her own network, OWN. Winfrey is widely regarded as one of the most influential and inspiring women in the world, having interviewed countless celebrities, politicians, and ordinary people on her show.",
      "email": "entertainment@news.com"
    }
  ]
}
```

* Pageable wrong sort params 406

```json
{
  "exception": "PropertyReferenceException",
  "error_message": "No property 'tit' found for type 'News'; Did you mean 'id','text','time','title'",
  "error_code": "406 NOT_ACCEPTABLE"
}
```

#### GET findAllByMatchingTextParams | Find all News by matching text and title params with pagination

* Request example findAllByMatchingTextParams [news.http](news-service/src/main/resources/http/news.http)
* Response examples:
* List of News retrieved successfully 200

```json
{
  "news": [
    {
      "id": "20",
      "time": "2023-06-14T12:05:00",
      "title": "New scandal by Donald Trump shocks nation",
      "text": "The new scandal by the former president Donald Trump, involving his alleged involvement in a bribery scheme with a foreign leader, has shocked the nation and sparked outrage. The scandal, which was revealed by a whistleblower who leaked a phone call transcript between Trump and the leader of Ukraine, shows that Trump asked the leader to investigate his political rival Joe Biden in exchange for military aid. The scandal has led to calls for impeachment and criminal charges against Trump.",
      "email": "politics@news.com"
    },
    {
      "id": "19",
      "time": "2023-06-14T12:00:00",
      "title": "New song by Ed Sheeran tops charts",
      "text": "The new song by the popular singer-songwriter Ed Sheeran, titled Perfect Harmony, has topped the charts in several countries around the world. The song, which is a duet with his wife Cherry Seaborn, is a romantic ballad that expresses their love and happiness. The song has been praised for its melody, lyrics, and vocals.",
      "email": "music@news.com"
    }
  ]
}
```

* Pageable wrong sort params 406

```json
{
  "exception": "PropertyReferenceException",
  "error_message": "No property 'i' found for type 'News'; Did you mean 'id'",
  "error_code": "406 NOT_ACCEPTABLE"
}
```

#### POST save | Save new News

* Request example save [news.http](news-service/src/main/resources/http/news.http)
* Response examples:
* News saved successfully 201

```json
{
  "id": "21",
  "time": "2023-06-16T14:15:57",
  "title": "Hello from Belarus",
  "text": "Belarus is a great country!",
  "email": "Green@mail.com"
}
```

* Not Authenticated User 401

```json
{
  "exception": "InsufficientAuthenticationException",
  "error_message": "Full authentication is required to access this resource",
  "error_code": "401 UNAUTHORIZED"
}
```

* Access denied for User with this role 403

```json
{
  "exception": "AccessDeniedForThisRoleException",
  "error_message": "Access Denied for role: SUBSCRIBER",
  "error_code": "403 FORBIDDEN"
}
```

* Validation error 409

```json
{
  "exception": "ProtoValidationException",
  "error_message": ".NewsRequest.text: length must be at least 3 but got: 2 - Got \"Be\"",
  "error_code": "409 CONFLICT"
}
```

#### PUT updateById | Update News by id

* Request example update [news.http](news-service/src/main/resources/http/news.http)
* Response examples:
* News updated successfully 201

```.json
{
  "id": "21",
  "time": "2023-06-16T14:23:06",
  "title": "Good-bye from Belarus",
  "text": "Belarus says goodbye to you!",
  "email": "Green@mail.com"
}
```

* Not Authenticated User 401

```json
{
  "exception": "InsufficientAuthenticationException",
  "error_message": "Full authentication is required to access this resource",
  "error_code": "401 UNAUTHORIZED"
}
```

* Access denied for User with this role 403

```json
{
  "exception": "AccessDeniedForThisRoleException",
  "error_message": "Access Denied for role: SUBSCRIBER",
  "error_code": "403 FORBIDDEN"
}
```

* No News with this id in database 404

```.json
{
  "exception": "NoSuchNewsException",
  "error_message": "There is no News with ID 211 to update",
  "error_code": "404 NOT_FOUND"
}
```

* User with this role does not have required permission 405

```.json
{
  "exception": "UserDoesNotHavePermissionException",
  "error_message": "With role JOURNALIST you can update or delete only your own news/comments",
  "error_code": "405 METHOD_NOT_ALLOWED"
}
```

* Validation error 409

```json
{
  "error_code": "409 CONFLICT",
  "violations": [
    {
      "field_name": "updateById.id",
      "error_message": "must be greater than 0"
    }
  ]
}
```

#### DELETE deleteById | Delete News by id with related Comments

* Request example update [news.http](news-service/src/main/resources/http/news.http)
* Response examples:
* News deleted successfully 200

```.json
{
  "message": "News with ID 21 was successfully deleted"
}
```

* Not Authenticated User 401

```.json
{
  "exception": "InsufficientAuthenticationException",
  "error_message": "Full authentication is required to access this resource",
  "error_code": "401 UNAUTHORIZED"
}
```

* Access denied for User with this role 403

```json
{
  "exception": "AccessDeniedForThisRoleException",
  "error_message": "Access Denied for role: SUBSCRIBER",
  "error_code": "403 FORBIDDEN"
}
```

* No News with this id to delete 404

```json
{
  "exception": "NoSuchNewsException",
  "error_message": "There is no News with ID 222 to delete",
  "error_code": "404 NOT_FOUND"
}
```

* User with this role does not have required permission 405

```json
{
  "exception": "UserDoesNotHavePermissionException",
  "error_message": "With role JOURNALIST you can update or delete only your own news/comments",
  "error_code": "405 METHOD_NOT_ALLOWED"
}
```

* Validation error

```json
{
  "error_code": "409 CONFLICT",
  "violations": [
    {
      "field_name": "deleteById.id",
      "error_message": "must be greater than 0"
    }
  ]
}
```

***

### CommentController

***

#### GET findById | Find Comment by id

* Request example findById [comments.http](news-service/src/main/resources/http/comments.http)
* Response examples:
* Comment retrieved successfully 200

```json
{
  "id": "5",
  "time": "2023-06-14T10:35:00",
  "text": "This is so cool! I wish I could see it in person!",
  "username": "VolcanoFan",
  "email": "volcanofan@gmail.com"
}
```

* No Comment with this id in database 404

```json
{
  "exception": "NoSuchCommentException",
  "error_message": "Comment with ID 233 does not exist",
  "error_code": "404 NOT_FOUND"
}
```

* Validation error 409

```.json
{
  "error_code": "409 CONFLICT",
  "violations": [
    {
      "field_name": "findById.id",
      "error_message": "must be greater than 0"
    }
  ]
}
```

#### GET findNewsByNewsIdWithComments | Find News by newsId with its Comments with pagination

* Request example findNewsByNewsIdWithComments [comments.http](news-service/src/main/resources/http/comments.http)
* Response examples:
* News with Comments retrieved successfully 200

```.json
{
  "id": "19",
  "time": "2023-06-14T12:00:00",
  "title": "New song by Ed Sheeran tops charts",
  "text": "The new song by the popular singer-songwriter Ed Sheeran, titled Perfect Harmony, has topped the charts in several countries around the world. The song, which is a duet with his wife Cherry Seaborn, is a romantic ballad that expresses their love and happiness. The song has been praised for its melody, lyrics, and vocals.",
  "email": "music@news.com",
  "comments": [
    {
      "id": "92",
      "time": "2023-06-14T12:02:00",
      "text": "This is so cool! The song is so beautiful!",
      "username": "SheeranLover",
      "email": "sheeranlover@yahoo.com"
    },
    {
      "id": "143",
      "time": "2023-06-14T12:03:00",
      "text": "This is so lame. Ed Sheeran is boring.",
      "username": "SheeranHater2",
      "email": "sheeranhater2@outlook.com"
    },
    {
      "id": "93",
      "time": "2023-06-14T12:03:00",
      "text": "This is so lame. Ed Sheeran is boring.",
      "username": "SheeranHater",
      "email": "sheeranhater@outlook.com"
    },
    {
      "id": "141",
      "time": "2023-06-14T12:01:00",
      "text": "Wow! That's awesome! I love Ed Sheeran!",
      "username": "SheeranFan2",
      "email": "sheeranfan2@gmail.com"
    },
    {
      "id": "91",
      "time": "2023-06-14T12:01:00",
      "text": "Wow! That's awesome! I love Ed Sheeran!",
      "username": "SheeranFan",
      "email": "sheeranfan@gmail.com"
    }
  ]
}
```

* No News with this newsId in database 404

```.json
{
  "exception": "NoSuchNewsException",
  "error_message": "News with ID 22 does not exist",
  "error_code": "404 NOT_FOUND"
}
```

* Pageable wrong sort params 406

```.json
{
  "exception": "PropertyReferenceException",
  "error_message": "No property 'usename' found for type 'Comment'; Did you mean 'username'",
  "error_code": "406 NOT_ACCEPTABLE"
}
```

* Validation error 409

```.json
 {
  "error_code": "409 CONFLICT",
  "violations": [
    {
      "field_name": "findNewsByNewsIdWithComments.newsId",
      "error_message": "must be greater than 0"
    }
  ]
}
```

#### GET findAllByMatchingTextParams | Find all Comments by matching text and username params with pagination

* Request example findAllByMatchingTextParams [comments.http](news-service/src/main/resources/http/comments.http)
* Response examples:
* List of Comments retrieved successfully 200

```.json
{
  "comments": [
    {
      "id": "37",
      "time": "2023-06-14T11:07:00",
      "text": "This is so cool! I wonder what it looked like.",
      "username": "DinoLover",
      "email": "dinolover@yahoo.com"
    },
    {
      "id": "162",
      "time": "2023-06-14T11:07:00",
      "text": "This is so cool! I wonder what it looked like.",
      "username": "DinoLover2",
      "email": "dinolover2@yahoo.com"
    }
  ]
}
```

* Pageable wrong sort params 406

```.json
{
  "exception": "PropertyReferenceException",
  "error_message": "No property 'tex' found for type 'Comment'; Did you mean 'text'",
  "error_code": "406 NOT_ACCEPTABLE"
}
```

#### POST Save | Save new Comment and related it with News by newsId

* Request example save [comments.http](news-service/src/main/resources/http/comments.http)
* Response examples:
* Comment saved successfully 201

```.json
{
  "id": "203",
  "time": "2023-06-16T14:31:34",
  "text": "Not bad at all :(",
  "username": "Vasiliy",
  "email": "Green@mail.com"
}
```

* Not Authenticated User 401

```.json
{
  "exception": "InsufficientAuthenticationException",
  "error_message": "Full authentication is required to access this resource",
  "error_code": "401 UNAUTHORIZED"
}
```

* Access denied for User with this role 403

```.json
{
  "exception": "AccessDeniedForThisRoleException",
  "error_message": "Access Denied for role: JOURNALIST",
  "error_code": "403 FORBIDDEN"
}
```

* No News with this newsId in database 404

```.json
{
  "exception": "NoSuchNewsException",
  "error_message": "News with ID 22 does not exist",
  "error_code": "404 NOT_FOUND"
}
```

* Validation error 409

```.json
{
  "exception": "ProtoValidationException",
  "error_message": ".CommentWithNewsRequest.news_id: must be greater than 0 - Got 0",
  "error_code": "409 CONFLICT"
}
```

#### PUT updateById | Update Comment by id

* Request example update [comments.http](news-service/src/main/resources/http/comments.http)
* Response examples:
* Comment updated successfully 201

```.json
{
  "id": "191",
  "time": "2023-06-16T14:32:55",
  "text": "Wow, that's cool!",
  "username": "Svetlana",
  "email": "Green@mail.com"
}
```

* Not Authenticated User 401

```.json
{
  "exception": "InsufficientAuthenticationException",
  "error_message": "Full authentication is required to access this resource",
  "error_code": "401 UNAUTHORIZED"
}
```

* Access denied for User with this role 403

```.json
{
  "exception": "AccessDeniedForThisRoleException",
  "error_message": "Access Denied for role: JOURNALIST",
  "error_code": "403 FORBIDDEN"
}
```

* No Comment with this id in database 404

```.json
{
  "exception": "NoSuchCommentException",
  "error_message": "There is no Comment with ID 333 to update",
  "error_code": "404 NOT_FOUND"
}
```

* User with this role does not have required permission 405

```.json
{
  "exception": "UserDoesNotHavePermissionException",
  "error_message": "With role SUBSCRIBER you can update or delete only your own news/comments",
  "error_code": "405 METHOD_NOT_ALLOWED"
}
```

* Validation error 409

```.json
{
  "exception": "ProtoValidationException",
  "error_message": ".CommentRequest.username: must match pattern ^[a-zA-Zа-яА-ЯёЁ0-9@_-]+$ - Got \"Svetlana111   \"",
  "error_code": "409 CONFLICT"
}
```

#### DELETE deleteById | Delete Comment by id

* Request example delete [comments.http](news-service/src/main/resources/http/comments.http)
* Response examples:
* Comment deleted successfully 200

```.json
{
  "message": "Comment with ID 191 was successfully deleted"
}
```

* Not Authenticated User 401

```.json
{
  "exception": "InsufficientAuthenticationException",
  "error_message": "Full authentication is required to access this resource",
  "error_code": "401 UNAUTHORIZED"
}
```

* Access denied for User with this role 403

```.json
{
  "exception": "AccessDeniedForThisRoleException",
  "error_message": "Access Denied for role: JOURNALIST",
  "error_code": "403 FORBIDDEN"
}
```

* Not Tag with this id to delete 404

```.json
{
  "exception": "NoSuchCommentException",
  "error_message": "There is no Comment with ID 456 to delete",
  "error_code": "404 NOT_FOUND"
}
```

* User with this role does not have required permission 405

```.json
{
  "exception": "UserDoesNotHavePermissionException",
  "error_message": "With role SUBSCRIBER you can update or delete only your own news/comments",
  "error_code": "405 METHOD_NOT_ALLOWED"
}
```

* Validation error 409

```.json
{
  "error_code": "409 CONFLICT",
  "violations": [
    {
      "field_name": "deleteById.id",
      "error_message": "must be greater than 0"
    }
  ]
}
```

***

### AuthenticationController

***

#### POST register | Register, save new User and get jwt token

* Request example register [auth.http](news-service/src/main/resources/http/auth.http)
* Response examples:
* User registered successfully 201

```.json
{
  "id": "4",
  "firstname": "Pavel",
  "lastname": "Shishkin",
  "email": "Green@mail.com",
  "role": "ADMIN",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiQURNSU4ifV0sInN1YiI6IkdyZWVuQG1haWwuY29tIiwiaWF0IjoxNjg2MzExNDIxLCJleHAiOjE2ODYzOTc4MjF9.CPPgPruJXiABRI2466pT8jX62L9t9BTV00WuC_AHrNA",
  "tokenExpiration": "Sat Jun 10 14:50:21 MSK 2023",
  "createdTime": "2023-06-09T14:50:21",
  "updatedTime": "2023-06-09T14:50:21"
}
```

* Email is unique for User 406

```.json
{
  "exception": "UniqueEmailException",
  "error_message": "Email Green@mail.com is occupied! Another user is already registered by this email!",
  "error_code": "406 NOT_ACCEPTABLE"
}
```

* Validation error 409

```.json
{
  "exception": "ProtoValidationException",
  "error_message": ".UserRegisterRequest.role: must match pattern ADMIN|JOURNALIST|SUBSCRIBER - Got \"SUPERMAN\"",
  "error_code": "409 CONFLICT"
}
```

#### POST authenticate | Authenticate User and get jwt token

* Request example authenticate [auth.http](news-service/src/main/resources/http/auth.http)
* Response examples:
* User authenticated successfully 200

```.json
 {
  "id": "1",
  "firstname": "Chuck",
  "lastname": "Norris",
  "email": "ChakcNunChuck@gmail.com",
  "role": "SUBSCRIBER",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiU1VCU0NSSUJFUiJ9XSwic3ViIjoiQ2hha2NOdW5DaHVja0BnbWFpbC5jb20iLCJpYXQiOjE2ODY5MTUzNjcsImV4cCI6MTY4NzAwMTc2N30.zTK3gwck5_SSORSufAfbON8UO4cOcFe4-xJTAFS9dsc",
  "tokenExpiration": "Sat Jun 17 14:36:07 MSK 2023",
  "createdTime": "2023-06-06T16:45:59",
  "updatedTime": "2023-06-06T16:45:59"
}
```

* Wrong password for User 401

```.json
{
  "exception": "UserApiClientException",
  "error_message": "Bad credentials",
  "error_code": "401 UNAUTHORIZED"
}
```

* No User with this email in database 404

```.json
{
  "exception": "NoSuchUserEmailException",
  "error_message": "User with email ChakcNunChuck@1gmail.com is not exist",
  "error_code": "404 NOT_FOUND"
}
```

* Validation error 409

```.json
{
  "exception": "ProtoValidationException",
  "error_message": ".UserAuthenticationRequest.email: should be a valid email - Got \"ChakcNunChuckgmail.com\"",
  "error_code": "409 CONFLICT"
}
```

#### PUT updateByToken | Update User by token

* Request example updateByToken [auth.http](news-service/src/main/resources/http/auth.http)
* Response examples:
* User updated successfully 201

```.json
{
  "id": "2",
  "firstname": "Igor",
  "lastname": "Zavadskiy",
  "email": "Shwarsz@yahoo.com",
  "role": "JOURNALIST",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiSk9VUk5BTElTVCJ9XSwic3ViIjoiU2h3YXJzekB5YWhvby5jb20iLCJpYXQiOjE2ODYzMTA0NTUsImV4cCI6MTY4NjM5Njg1NX0.3xmnKRAPcAjPNo_kN8myvlOfyGaweGgjt9DFhfa8LuQ",
  "tokenExpiration": "Sat Jun 10 14:34:15 MSK 2023",
  "createdTime": "2023-06-06T12:33:47",
  "updatedTime": "2023-06-09T15:05:50"
}
```

* Not Authenticated User 401

```.json
{
  "exception": "UserApiClientException",
  "error_message": "Bad credentials",
  "error_code": "401 UNAUTHORIZED"
}
```

* No User with this email in database 404

```.json
{
  "exception": "NoSuchUserEmailException",
  "error_message": "User with email ChakcNunChuck@1gmail.com is not exist",
  "error_code": "404 NOT_FOUND"
}
```

* Validation error 409

```.json
{
  "exception": "ProtoValidationException",
  "error_message": ".UserUpdateRequest.firstname: must match pattern ^[a-zA-Zа-яА-ЯёЁ]+$ - Got \"Igor1\"",
  "error_code": "409 CONFLICT"
}
```

#### DELETE deleteByToken | Delete User by token

* Request example deleteByToken [auth.http](news-service/src/main/resources/http/auth.http)
* Response examples
* User deleted successfully 200

```.json
{
  "message": "User with email Shwarsz@yahoo.com was successfully deleted"
}
```

* Not Authenticated User 401

```.json
{
  "exception": "InsufficientAuthenticationException",
  "error_message": "Full authentication is required to access this resource",
  "error_code": "401 UNAUTHORIZED"
}
```

* No User with this email in database 404

```.json
{
  "exception": "NoSuchUserEmailException",
  "error_message": "User with email ChakcNunChuck@1gmail.com is not exist",
  "error_code": "404 NOT_FOUND"
}
```

***

### UserController

***

#### POST register | Register, save new User and get jwt token

* Request example register [users.http](user-service/src/main/resources/http/users.http)
* Response examples:
* User registered successfully 201

```.json
{
  "id": "4",
  "firstname": "Pavel",
  "lastname": "Shishkin",
  "email": "Green@mail.com",
  "role": "ADMIN",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiQURNSU4ifV0sInN1YiI6IkdyZWVuQG1haWwuY29tIiwiaWF0IjoxNjg2MzExNDIxLCJleHAiOjE2ODYzOTc4MjF9.CPPgPruJXiABRI2466pT8jX62L9t9BTV00WuC_AHrNA",
  "tokenExpiration": "Sat Jun 10 14:50:21 MSK 2023",
  "createdTime": "2023-06-09T14:50:21",
  "updatedTime": "2023-06-09T14:50:21"
}
```

* Email is unique for User 406

```.json
{
  "exception": "UniqueEmailException",
  "error_message": "Email Green@mail.com is occupied! Another user is already registered by this email!",
  "error_code": "406 NOT_ACCEPTABLE"
}
```

* Validation error 409

```.json
{
  "exception": "ProtoValidationException",
  "error_message": ".UserRegisterRequest.role: must match pattern ADMIN|JOURNALIST|SUBSCRIBER - Got \"SUPERMAN\"",
  "error_code": "409 CONFLICT"
}
```

#### POST authenticate | Authenticate User and get jwt token

* Request example authenticate [users.http](user-service/src/main/resources/http/users.http)
* Response examples:
* User authenticated successfully 200

```.json
 {
  "id": "1",
  "firstname": "Chuck",
  "lastname": "Norris",
  "email": "ChakcNunChuck@gmail.com",
  "role": "SUBSCRIBER",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiU1VCU0NSSUJFUiJ9XSwic3ViIjoiQ2hha2NOdW5DaHVja0BnbWFpbC5jb20iLCJpYXQiOjE2ODY5MTUzNjcsImV4cCI6MTY4NzAwMTc2N30.zTK3gwck5_SSORSufAfbON8UO4cOcFe4-xJTAFS9dsc",
  "tokenExpiration": "Sat Jun 17 14:36:07 MSK 2023",
  "createdTime": "2023-06-06T16:45:59",
  "updatedTime": "2023-06-06T16:45:59"
}
```

* Wrong password for User 401

```.json
{
  "exception": "UserApiClientException",
  "error_message": "Bad credentials",
  "error_code": "401 UNAUTHORIZED"
}
```

* No User with this email in database 404

```.json
{
  "exception": "NoSuchUserEmailException",
  "error_message": "User with email ChakcNunChuck@1gmail.com is not exist",
  "error_code": "404 NOT_FOUND"
}
```

* Validation error 409

```.json
{
  "exception": "ProtoValidationException",
  "error_message": ".UserAuthenticationRequest.email: should be a valid email - Got \"ChakcNunChuckgmail.com\"",
  "error_code": "409 CONFLICT"
}
```

#### POST tokenValidationCheck | Check validation of token for User, if valid - returns his role and email

* Request example validate [users.http](user-service/src/main/resources/http/users.http)
* Response examples
* Validate successfully 200

```.json
{
  "role": "JOURNALIST",
  "email": "Shwarsz@yahoo.com"
}
```

* Not Authenticated User or damaged token 401

```.json
{
  "exception": "SignatureException",
  "error_message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted.",
  "error_code": "401 UNAUTHORIZED"
}
```

#### PUT updateByToken | Update User by token

* Request example updateByToken [users.http](user-service/src/main/resources/http/users.http)
* Response examples:
* User updated successfully 201

```.json
{
  "id": "2",
  "firstname": "Igor",
  "lastname": "Zavadskiy",
  "email": "Shwarsz@yahoo.com",
  "role": "JOURNALIST",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6W3siYXV0aG9yaXR5IjoiSk9VUk5BTElTVCJ9XSwic3ViIjoiU2h3YXJzekB5YWhvby5jb20iLCJpYXQiOjE2ODYzMTA0NTUsImV4cCI6MTY4NjM5Njg1NX0.3xmnKRAPcAjPNo_kN8myvlOfyGaweGgjt9DFhfa8LuQ",
  "tokenExpiration": "Sat Jun 10 14:34:15 MSK 2023",
  "createdTime": "2023-06-06T12:33:47",
  "updatedTime": "2023-06-09T15:05:50"
}
```

* Not Authenticated User 401

```.json
{
  "exception": "UserApiClientException",
  "error_message": "Bad credentials",
  "error_code": "401 UNAUTHORIZED"
}
```

* No User with this email in database 404

```.json
{
  "exception": "NoSuchUserEmailException",
  "error_message": "User with email ChakcNunChuck@1gmail.com is not exist",
  "error_code": "404 NOT_FOUND"
}
```

* Validation error 409

```.json
{
  "exception": "ProtoValidationException",
  "error_message": ".UserUpdateRequest.firstname: must match pattern ^[a-zA-Zа-яА-ЯёЁ]+$ - Got \"Igor1\"",
  "error_code": "409 CONFLICT"
}
```

#### DELETE deleteByToken | Delete User by token

* Request example deleteByToken [users.http](user-service/src/main/resources/http/users.http)
* Response examples
* User deleted successfully 200

```.json
{
  "message": "User with email Shwarsz@yahoo.com was successfully deleted"
}
```

* Not Authenticated User 401

```.json
{
  "exception": "InsufficientAuthenticationException",
  "error_message": "Full authentication is required to access this resource",
  "error_code": "401 UNAUTHORIZED"
}
```

* No User with this email in database 404

```.json
{
  "exception": "NoSuchUserEmailException",
  "error_message": "User with email ChakcNunChuck@1gmail.com is not exist",
  "error_code": "404 NOT_FOUND"
}
```
