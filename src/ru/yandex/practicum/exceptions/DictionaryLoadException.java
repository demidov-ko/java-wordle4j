package ru.yandex.practicum.exceptions;

public class DictionaryLoadException extends WordleIOException {
    public DictionaryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}