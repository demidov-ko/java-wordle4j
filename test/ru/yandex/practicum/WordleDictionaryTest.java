package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.wordle_auxiliary_classes.WordleDictionary;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(Arrays.asList("КОТЁЛ", "ТАКСИ", "ЗАБЕГ", "КАБАН"));
    }

    @Test
    void testWordMatchesAllConstraints() {
        String word = "КОТЁЛ";
        Set<Character> absentLetters = Set.of('М', 'Н');
        Map<Integer, Character> correctLetters = Map.of(0, 'К', 2, 'Т');
        Map<Integer, Set<Character>> presentLetters = Map.of(3, Set.of('О'));

        boolean result = dictionary.matchesConstraints(word, absentLetters, correctLetters, presentLetters);
        assertTrue(result, "Слово должно удовлетворять всем ограничениям");
    }

    @Test
    void testWordContainsAbsentLetter() {
        String word = "КОТЕМ";
        Set<Character> absentLetters = Set.of('М');
        Map<Integer, Character> correctLetters = Map.of();
        Map<Integer, Set<Character>> presentLetters = Map.of();

        boolean result = dictionary.matchesConstraints(word, absentLetters, correctLetters, presentLetters);

        assertFalse(result, "Слово не должно содержать отсутствующих букв");
    }

    @Test
    void testCorrectLetterNotInPlace() {
        String word = "БОТЁЛ";
        Set<Character> absentLetters = Set.of();
        Map<Integer, Character> correctLetters = Map.of(0, 'К'); // должно быть 'К' на 0‑й
        Map<Integer, Set<Character>> presentLetters = Map.of();

        boolean result = dictionary.matchesConstraints(word, absentLetters, correctLetters, presentLetters);
        assertFalse(result, "Буква на позиции должна совпадать с ожидаемой");
    }

    @Test
    void testNoConstraintsReturnsAllWords() {
        Set<Character> absentLetters = new HashSet<>();
        Map<Integer, Character> correctLetters = new HashMap<>();
        Map<Integer, Set<Character>> presentLetters = new HashMap<>();

        List<String> result = dictionary.getPossibleWords(absentLetters, correctLetters, presentLetters);
        assertEquals(Arrays.asList("КОТЁЛ", "ТАКСИ", "ЗАБЕГ", "КАБАН"), result);
    }

    @Test
    void testAbsentLettersFilterOutWords() {
        Set<Character> absentLetters = Set.of('К');
        Map<Integer, Character> correctLetters = new HashMap<>();
        Map<Integer, Set<Character>> presentLetters = new HashMap<>();

        List<String> result = dictionary.getPossibleWords
                (absentLetters, correctLetters, presentLetters);

        assertEquals(List.of("ЗАБЕГ"), result);
    }

    @Test
    void testCorrectLettersMatchPositions() {
        Set<Character> absentLetters = new HashSet<>();
        Map<Integer, Character> correctLetters = Map.of(0, 'К');
        Map<Integer, Set<Character>> presentLetters = new HashMap<>();

        List<String> result = dictionary.getPossibleWords
                (absentLetters, correctLetters, presentLetters);

        assertEquals(Arrays.asList("КОТЁЛ", "КАБАН"), result);
    }

    @Test
    void testExactMatchReturnsTrue() {
        String word = "КОТЁЛ";
        String guess = "КОТЁЛ";

        boolean result = dictionary.matchesGuess(word, guess);
        assertTrue(result, "При полном совпадении должно возвращаться true");
    }

    @Test
    void testExactMatchReturnsFalse() {
        String word = "КОТЁЛ";
        String guess = "КОТЯЛ";

        boolean result = dictionary.matchesGuess(word, guess);
        assertFalse(result, "При неполном совпадении должно возвращаться false");
    }

    @Test
    void testNoGuessesReturnsTrue() {
        String word = "КОТЁЛ";
        List<String> guesses = List.of();

        boolean result = dictionary.matchesAllGuesses(word, guesses);
        assertTrue(result, "При пустом списке попыток должно возвращаться true");
    }

    @Test
    void testGuessesPassReturnsTrue() {
        String word = "КОТЁЛ";
        List<String> guesses = Arrays.asList("КОТЁЛ");

        boolean result = dictionary.matchesAllGuesses(word, guesses);

        assertTrue(result, "Если все попытки проходят, должно возвращаться true");
    }

    @Test
    void testAllGuessesFailReturnsFalse() {
        String word = "КОТЁЛ";
        List<String> guesses = Arrays.asList("КАЛАЧ", "КАЗНЬ", "КАДЕТ");

        boolean result = dictionary.matchesAllGuesses(word, guesses);
        assertFalse(result, "Если все попытки не проходят, должно возвращаться false");
    }
}
