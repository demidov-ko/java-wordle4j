package ru.yandex.practicum.exceptions;

public class MaxAttemptsExceeded extends WordleGameException {
    public MaxAttemptsExceeded() {
        super("Превышено число попыток");
    }
}
