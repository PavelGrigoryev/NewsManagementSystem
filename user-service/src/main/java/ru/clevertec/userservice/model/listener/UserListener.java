package ru.clevertec.userservice.model.listener;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import ru.clevertec.userservice.model.User;

import java.time.LocalDateTime;

public class UserListener {

    @PrePersist
    public void prePersist(User user) {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedTime(now);
        user.setUpdatedTime(now);
    }

    @PreUpdate
    public void preUpdate(User user) {
        user.setUpdatedTime(LocalDateTime.now());
    }

}
