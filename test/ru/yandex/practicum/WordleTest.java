package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

class WordleTest {

    private static final String TEST_DICT = "test_words.txt";
    private static final String TEST_LOG = "wordle.log";
    private static final String TARGET_WORD = "КОТЁЛ";

    @BeforeEach
    void setUp() throws IOException {
        List<String> words = Arrays.asList(TARGET_WORD);
        Files.write(Paths.get(TEST_DICT), words);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get(TEST_DICT));
    }

    @Test
    void testHintRequested() throws IOException {
        assertTrue(Files.exists(Paths.get(TEST_DICT)), "Словарь не создан");

        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Wordle.main(new String[]{});

        if (!Files.exists(Paths.get(TEST_LOG))) {
            System.out.println("Лог не создан! Содержимое директории:");
            try {
                Files.list(Paths.get(".")).forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fail("Лог‑файл не создан");
        }

        List<String> log = Files.readAllLines(Paths.get(TEST_LOG));
        boolean found = log.stream().anyMatch(line ->
                line.contains("Сгенерирована подсказка:") ||
                        line.contains("Подсказка:")
        );
        assertTrue(found, "В логе нет сообщения о подсказке. Лог:\n" + String.join("\n", log));
    }

}
