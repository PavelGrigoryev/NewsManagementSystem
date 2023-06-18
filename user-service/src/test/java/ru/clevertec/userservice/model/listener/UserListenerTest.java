package ru.clevertec.userservice.model.listener;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.clevertec.userservice.model.User;
import ru.clevertec.userservice.util.testbuilder.UserTestBuilder;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserListenerTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = UserTestBuilder.aUser().build();
    }

    @Test
    @DisplayName("test prePersist should insert LocalDateTime")
    void testPrePersistShouldInsertLocalDateTime() {
        UserListener userListener = new UserListener();

        userListener.prePersist(user);

        assertAll(
                () -> assertThat(LocalDateTime.now().getYear()).isEqualTo(user.getCreatedTime().getYear()),
                () -> assertThat(LocalDateTime.now().getMonthValue()).isEqualTo(user.getCreatedTime().getMonthValue()),
                () -> assertThat(LocalDateTime.now().getDayOfMonth()).isEqualTo(user.getCreatedTime().getDayOfMonth()),
                () -> assertThat(LocalDateTime.now().getHour()).isEqualTo(user.getCreatedTime().getHour()),
                () -> assertThat(LocalDateTime.now().getMinute()).isEqualTo(user.getCreatedTime().getMinute()),
                () -> assertThat(LocalDateTime.now().getYear()).isEqualTo(user.getUpdatedTime().getYear()),
                () -> assertThat(LocalDateTime.now().getMonthValue()).isEqualTo(user.getUpdatedTime().getMonthValue()),
                () -> assertThat(LocalDateTime.now().getDayOfMonth()).isEqualTo(user.getUpdatedTime().getDayOfMonth()),
                () -> assertThat(LocalDateTime.now().getHour()).isEqualTo(user.getUpdatedTime().getHour()),
                () -> assertThat(LocalDateTime.now().getMinute()).isEqualTo(user.getUpdatedTime().getMinute())
        );
    }

    @Test
    @DisplayName("test preUpdate should update LocalDateTime")
    void testPreUpdateShouldUpdateLocalDateTime() {
        UserListener userListener = new UserListener();

        userListener.preUpdate(user);

        assertAll(
                () -> assertThat(LocalDateTime.now().getYear()).isEqualTo(user.getUpdatedTime().getYear()),
                () -> assertThat(LocalDateTime.now().getMonthValue()).isEqualTo(user.getUpdatedTime().getMonthValue()),
                () -> assertThat(LocalDateTime.now().getDayOfMonth()).isEqualTo(user.getUpdatedTime().getDayOfMonth()),
                () -> assertThat(LocalDateTime.now().getHour()).isEqualTo(user.getUpdatedTime().getHour()),
                () -> assertThat(LocalDateTime.now().getMinute()).isEqualTo(user.getUpdatedTime().getMinute())
        );
    }

}