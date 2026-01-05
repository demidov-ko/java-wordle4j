package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.WordleGameException;
import ru.yandex.practicum.wordle_auxiliary_classes.GameLogger;
import ru.yandex.practicum.wordle_auxiliary_classes.WordleDictionary;
import ru.yandex.practicum.wordle_auxiliary_classes.WordleGame;
//import ru.yandex.practicum.exceptions.GameOverException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private WordleGame game;
    private GameLogger logger;
    private PrintWriter printWriter;
    private StringWriter stringWriter;
    private static final String TARGET_WORD = "ТАКСИ";

    @BeforeEach
    void setUp() {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        logger = new GameLogger(printWriter);

        WordleDictionary dictionary = new WordleDictionary(List.of(TARGET_WORD));
        game = new WordleGame(dictionary, logger);
    }

    @Test
    void testTtheInitialStateOfTheGame() {
        assertEquals(TARGET_WORD, game.getTargetWord());
        assertEquals(6, game.getRemainingAttempts());
        assertFalse(game.isWon());
        assertFalse(game.isLost());
    }

    @Test
    void testCorrectGuessLogsSuccess() {
        game.makeGuess("ТАКСИ");

        assertTrue(game.isWon(), "Игра должна быть выиграна при верном слове");
        assertFalse(game.isLost(), "Игра не должна быть проиграна при верном слове");
        assertEquals(5, game.getRemainingAttempts(), "Должно остаться 5 попыток после верной попытки");
    }

    @Test
    void testIncorrectGuessLogsAttempt() {
        game.makeGuess("СЕКТА");

        String logOutput = stringWriter.toString();
        assertTrue(logOutput.contains("[WARNING] Слово СЕКТА не найдено в словаре"),
                "В логах должно быть предупреждение о отсутствии слова в словаре");
    }

    @Test
    void testNormalizeAndValidateOnlyRussianСharacters() {
        String word = "fruit";

        WordleGameException exception = assertThrows(WordleGameException.class, () -> game.normalizeAndValidate(word));
        assertTrue(exception.getMessage().contains("Слово должно содержать только русские буквы"));
    }
}