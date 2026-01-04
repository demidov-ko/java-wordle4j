package ru.yandex.practicum.exceptions;

// Неигровые (системные) исключения
public class WordleIOException extends RuntimeException {
    public WordleIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
