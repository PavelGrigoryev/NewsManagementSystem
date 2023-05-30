package ru.clevertec.newsservice.util.testbuilder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.With;
import org.springframework.data.domain.ExampleMatcher;
import ru.clevertec.newsservice.util.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aExampleMatcher")
@With
public class ExampleMatcherTestBuilder implements TestBuilder<ExampleMatcher> {

    private ExampleMatcher exampleMatcher = ExampleMatcher.matchingAll()
            .withIgnoreNullValues()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

    @Override
    public ExampleMatcher build() {
        return exampleMatcher;
    }

}
