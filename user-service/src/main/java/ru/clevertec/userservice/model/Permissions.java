package ru.clevertec.userservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permissions {

    NEWS_OPERATIONS("news operations"),
    COMMENT_OPERATIONS("comment operations");

    @Getter
    private final String permission;

}
