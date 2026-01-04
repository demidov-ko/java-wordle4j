package ru.yandex.practicum.exceptions;

// Игровые исключения
public class WordNotFoundInDictionary extends WordleGameException {
    public WordNotFoundInDictionary(String word) {
        super("Слово '" + word + "' не найдено в словаре");
    }
}