package ru.yandex.practicum.exceptions;

public class InvalidWordLength extends WordleGameException {
    public InvalidWordLength(int actual, int expected) {
        super("Длина слова: " + actual + ", ожидается: " + expected);
    }
}