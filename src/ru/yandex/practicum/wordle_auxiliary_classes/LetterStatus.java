package ru.yandex.practicum.wordleAuxiliaryСlasses;

public enum LetterStatus {
    CORRECT("\uD83D\uDFE9"),
    PRESENT("\uD83D\uDFE8"),
    ABSENT("\uD83D\uDFE5");

    public final String symbol;

    LetterStatus(String symbol) {
        this.symbol = symbol;
    }
}
