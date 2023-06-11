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
import ru.clevertec.newsservice.cache.LRUCache;
import ru.clevertec.newsservice.cache.factory.CacheFactory;
import ru.clevertec.newsservice.dto.DeleteResponse;
import ru.clevertec.newsservice.dto.news.NewsRequest;
import ru.clevertec.newsservice.dto.news.NewsResponse;
import ru.clevertec.newsservice.service.NewsService;
import ru.clevertec.newsservice.service.impl.NewsServiceImpl;
import ru.clevertec.newsservice.util.testbuilder.news.NewsRequestTestBuilder;
import ru.clevertec.newsservice.util.testbuilder.news.NewsResponseTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class NewsCacheAspectTest {

    @InjectMocks
    private NewsCacheAspect newsCacheAspect;
    @Mock
    private CacheFactory<Object, Object> cacheFactory;
    @Mock
    private NewsServiceImpl newsService;
    private Cache<Object, Object> cache;
    private AspectJProxyFactory factory;

    @BeforeEach
    void setUp() {
        cache = new LRUCache<>(3);
        cache.put(1L, NewsResponseTestBuilder.aNewsResponse().build());
        factory = new AspectJProxyFactory(newsService);
        factory.addAspect(newsCacheAspect);
    }

    @Nested
    class AroundGetCacheableAnnotationTest {

        @Test
        @DisplayName("test aspect should return expected value after finding in service")
        void testAspectShouldReturnExpectedValueAfterFindingInService() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().withId(2L).build();
            long id = 2L;

            doReturn(cache)
                    .when(cacheFactory)
                    .createNewsCache();

            doReturn(expectedValue)
                    .when(newsService)
                    .findById(id);

            NewsService proxy = factory.getProxy();

            NewsResponse actualValue = proxy.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test aspect should put in cache")
        void testAspectShouldPutInCache() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().withId(2L).build();
            long id = 2L;

            doReturn(cache)
                    .when(cacheFactory)
                    .createNewsCache();

            doReturn(expectedValue)
                    .when(newsService)
                    .findById(id);

            NewsService proxy = factory.getProxy();

            proxy.findById(id);

            assertThat(cache.get(2L)).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test aspect should return expected value without finding ins service")
        void testAspectShouldReturnExpectedValueWithoutFindingInService() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().build();
            long id = 1L;

            doReturn(cache)
                    .when(cacheFactory)
                    .createNewsCache();

            NewsService proxy = factory.getProxy();

            NewsResponse actualValue = proxy.findById(id);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test aspect should get from cache")
        void testAspectShouldGetFromCache() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().build();
            long id = 1L;

            doReturn(cache)
                    .when(cacheFactory)
                    .createNewsCache();

            NewsService proxy = factory.getProxy();

            proxy.findById(id);

            assertThat(cache.get(1L)).isEqualTo(expectedValue);
        }

    }

    @Nested
    class AroundPutCacheableAnnotation {

        @Test
        @DisplayName("test aspect should return expected value")
        void testAspectShouldReturnExpectedValue() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().build();
            NewsRequest mockedNewsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            String token = "jwt";

            doReturn(cache)
                    .when(cacheFactory)
                    .createNewsCache();

            doReturn(expectedValue)
                    .when(newsService)
                    .save(mockedNewsRequest, token);

            NewsService proxy = factory.getProxy();

            NewsResponse actualValue = proxy.save(mockedNewsRequest, token);

            assertThat(actualValue).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("test aspect should put in cache")
        void testAspectShouldPutInCache() {
            NewsResponse expectedValue = NewsResponseTestBuilder.aNewsResponse().withId(2L).build();
            NewsRequest mockedNewsRequest = NewsRequestTestBuilder.aNewsRequest().build();
            String token = "jwt";

            doReturn(cache)
                    .when(cacheFactory)
                    .createNewsCache();

            doReturn(expectedValue)
                    .when(newsService)
                    .save(mockedNewsRequest, token);

            NewsService proxy = factory.getProxy();

            proxy.save(mockedNewsRequest, token);

            assertThat(cache.get(2L)).isEqualTo(expectedValue);
        }

    }

    @Nested
    class AroundRemoveCacheableAnnotation {

        @Test
        @DisplayName("test aspect should return expected value")
        void testAspectShouldReturnExpectedValue() {
            long id = 1L;
            DeleteResponse expectedValue = new DeleteResponse("News with ID " + id + " was successfully deleted");
            String token = "jwt";

            doReturn(cache)
                    .when(cacheFactory)
                    .createNewsCache();

            doReturn(expectedValue)
                    .when(newsService)
                    .deleteById(id, token);

            NewsService proxy = factory.getProxy();

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
                    .createNewsCache();

            NewsService proxy = factory.getProxy();

            proxy.deleteById(id, token);

            assertThat(cache).hasToString("{}");
        }

    }

}
