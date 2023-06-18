package ru.clevertec.newsservice.aop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import ru.clevertec.newsservice.cache.Cache;
import ru.clevertec.newsservice.cache.LFUCache;
import ru.clevertec.newsservice.cache.factory.CacheFactory;
import ru.clevertec.newsservice.dto.proto.CommentResponse;
import ru.clevertec.newsservice.dto.proto.CommentWithNewsRequest;
import ru.clevertec.newsservice.dto.proto.DeleteResponse;
import ru.clevertec.newsservice.service.CommentService;
import ru.clevertec.newsservice.service.impl.CommentServiceImpl;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentResponseTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.comment.CommentWithNewsRequestTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class CommentCacheAspectTest {

    @InjectMocks
    private CommentCacheAspect commentCacheAspect;
    @Mock
    private CacheFactory<Object, Object> cacheFactory;
    @Mock
    private CommentServiceImpl commentService;
    private Cache<Object, Object> cache;
    private AspectJProxyFactory factory;

    @BeforeEach
    void setUp() {
        cache = new LFUCache<>(3);
        cache.put(1L, CommentResponseTestBuilder.aCommentResponse().build());
        factory = new AspectJProxyFactory(commentService);
        factory.addAspect(commentCacheAspect);
    }

    @Nested
    class AroundGetCacheableAnnotationTest {

        @Test
        @DisplayName("test aspect should return expected value after finding in service")
        void testAspectShouldReturnExpectedValueAfterFindingInService() {
            long id = 2L;
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().withId(id).build();

            doReturn(cache)
                    .when(cacheFactory)
                    .createCommentCache();

            doReturn(expectedValue)
                    .when(commentService)
                    .findById(id);

            CommentService proxy = factory.getProxy();

            CommentResponse actualValue = proxy.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test aspect should put in cache")
        void testAspectShouldPutInCache() {
            long id = 2L;
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().withId(id).build();

            doReturn(cache)
                    .when(cacheFactory)
                    .createCommentCache();

            doReturn(expectedValue)
                    .when(commentService)
                    .findById(id);

            CommentService proxy = factory.getProxy();

            proxy.findById(id);

            assertThat(cache.get(id)).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test aspect should return expected value without finding in service")
        void testAspectShouldReturnExpectedValueWithoutFindingInService() {
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().build();
            long id = 1L;

            doReturn(cache)
                    .when(cacheFactory)
                    .createCommentCache();

            CommentService proxy = factory.getProxy();

            CommentResponse actualValue = proxy.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test aspect should get from cache")
        void testAspectShouldGetFromCache() {
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().build();
            long id = 1L;

            doReturn(cache)
                    .when(cacheFactory)
                    .createCommentCache();

            CommentService proxy = factory.getProxy();

            proxy.findById(id);

            assertThat(cache.get(id)).isEqualTo(expectedValue);
        }

    }

    @Nested
    class AroundPutCacheableAnnotation {

        @Test
        @DisplayName("test aspect should return expected value")
        void testAspectShouldReturnExpectedValue() {
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().build();
            CommentWithNewsRequest mockedRequest = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            String token = "jwt";

            doReturn(cache)
                    .when(cacheFactory)
                    .createCommentCache();

            doReturn(expectedValue)
                    .when(commentService)
                    .save(mockedRequest, token);

            CommentService proxy = factory.getProxy();

            CommentResponse actualValue = proxy.save(mockedRequest, token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test aspect should put in cache")
        void testAspectShouldPutInCache() {
            CommentResponse expectedValue = CommentResponseTestBuilder.aCommentResponse().withId(2L).build();
            CommentWithNewsRequest mockedRequest = CommentWithNewsRequestTestBuilder.aCommentWithNewsRequest().build();
            String token = "jwt";

            doReturn(cache)
                    .when(cacheFactory)
                    .createCommentCache();

            doReturn(expectedValue)
                    .when(commentService)
                    .save(mockedRequest, token);

            CommentService proxy = factory.getProxy();

            proxy.save(mockedRequest, token);

            assertThat(cache.get(2L)).isEqualTo(expectedValue);
        }

    }

    @Nested
    class AroundRemoveCacheableAnnotation {

        @Test
        @DisplayName("test aspect should return expected value")
        void testAspectShouldReturnExpectedValue() {
            long id = 1L;
            DeleteResponse expectedValue = DeleteResponse.newBuilder()
                    .setMessage("There is no Comment with ID " + id + " to delete")
                    .build();
            String token = "jwt";

            doReturn(cache)
                    .when(cacheFactory)
                    .createCommentCache();

            doReturn(expectedValue)
                    .when(commentService)
                    .deleteById(id, token);

            CommentService proxy = factory.getProxy();

            DeleteResponse actualValue = proxy.deleteById(id, token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test aspect should remove from cache")
        void testAspectShouldRemoveFromCache() {
            long id = 1L;
            String token = "jwt";

            doReturn(cache)
                    .when(cacheFactory)
                    .createCommentCache();

            CommentService proxy = factory.getProxy();

            proxy.deleteById(id, token);

            assertThat(cache.toString()).contains("cache={}, frequencies={}");
        }

    }

}
