package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

@Getter
public class ErrorResponse {
    // название ошибки
    String error;
    // подробное описание
    String description;

    public ErrorResponse(String error, String description) {
        this.error = error;
        this.description = description;
    }
}