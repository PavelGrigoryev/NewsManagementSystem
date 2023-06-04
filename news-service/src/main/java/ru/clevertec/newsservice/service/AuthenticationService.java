package ru.clevertec.newsservice.service;

public interface AuthenticationService {

    void isRoleAdminOrJournalist(String token);

}
