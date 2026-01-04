package ru.yandex.practicum.wordleAuxiliaryСlasses;

import java.util.*;

public class WordleDictionary {

    private final List<String> words;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>(words);
    }

    public String getRandomWord() {
        Random random = new Random();
        return words.get(random.nextInt(words.size())).toUpperCase();
    }

    public int size() {
        return words.size();
    }


    public boolean contains(String word) {
        return words.contains(word.toUpperCase());
    }

    //метод фильтрует слова с учётом предыдущих попыток
    public List<String> getPossibleWords(List<String> previousGuesses) {
        List<String> result = new ArrayList<>();

        for (String word : words) {
            if (matchesAllGuesses(word, previousGuesses)) {
                result.add(word);
            }
        }
        return result;
    }

    //метод проверяет совпадения слова по всем попыткам из списка guesses
    public boolean matchesAllGuesses(String word, List<String> guesses) {
        for (String guess : guesses) {
            if (!matchesGuess(word, guess)) {
                return false;
            }
        }
        return true;
    }

    //метод проверяет совпадение побуквенно
    public boolean matchesGuess(String word, String guess) {
        char[] wordChars = word.toCharArray();
        char[] guessChars = guess.toCharArray();
        for (int i = 0; i < 5; i++) {
            char c = guessChars[i];
            if (c == wordChars[i]) {
                continue; // буква на своём месте — ок
            }
            if (word.indexOf(c) == -1) {
                return false; // буквы нет в слове
            }
        }
        return true;
    }

    //метод получения подходящих слов
    public List<String> getPossibleWords(Set<Character> absentLetters,
                                         Map<Integer, Character> correctLetters,
                                         Map<Integer, Set<Character>> presentLetters) {
        List<String> result = new ArrayList<>();
        for (String word : words) {
            if (matchesConstraints(word, absentLetters, correctLetters, presentLetters)) {
                result.add(word);
            }
        }
        return result;
    }

    //метод проверки соответствия
    public boolean matchesConstraints(String word, Set<Character> absentLetters,
                                      Map<Integer, Character> correctLetters,
                                      Map<Integer, Set<Character>> presentLetters) {

        char[] chars = word.toCharArray();
        //проверяем отсутствующие буквы
        for (char c : absentLetters) {
            if (word.indexOf(c) != -1) return false;
        }
        // проверяем правильные буквы на своих местах
        for (Map.Entry<Integer, Character> entry : correctLetters.entrySet()) {
            int pos = entry.getKey();
            char expected = entry.getValue();
            if (chars[pos] != expected) return false;
        }
        // проверяем присутствующие буквы (не на своих местах)
        for (Map.Entry<Integer, Set<Character>> entry : presentLetters.entrySet()) {
            int pos = entry.getKey();
            Set<Character> expected = entry.getValue();
            char actual = chars[pos];
            if (expected.contains(actual)) {
                return false; // буква не должна быть на этой позиции
            }
            boolean found = false;
            for (char c : expected) {
                if (word.indexOf(c) != -1) found = true;
            }
            if (!found) return false;
        }
        return true;
    }
}