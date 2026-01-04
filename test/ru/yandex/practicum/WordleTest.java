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
        // Создаём тестовый словарь с одним словом
        List<String> words = Arrays.asList(TARGET_WORD);
        Files.write(Paths.get(TEST_DICT), words);
    }

    @AfterEach
    void tearDown() throws IOException {
        // Удаляем тестовые файлы после каждого теста
        Files.deleteIfExists(Paths.get(TEST_DICT));
    }

    /**
     * Тест 5: игрок запросил подсказку (пустая строка) → подсказка выдана
     */
    @Test
    void testHintRequested() throws IOException {
 // 1. Проверяем, что словарь создан
        assertTrue(Files.exists(Paths.get(TEST_DICT)), "Словарь не создан");

        // 2. Имитируем ввод
        String input = "\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        // 3. Запускаем игру
        Wordle.main(new String[]{});

        // 4. Проверяем лог
        if (!Files.exists(Paths.get(TEST_LOG))) {
            System.out.println("Лог не создан! Содержимое директории:");
            try {
                Files.list(Paths.get(".")).forEach(System.out::println);
            } catch (IOException e) {
                e.printStackTrace();
            }
            fail("Лог‑файл не создан");
        }

        // 5. Читаем лог и ищем подсказку
        List<String> log = Files.readAllLines(Paths.get(TEST_LOG));
        boolean found = log.stream().anyMatch(line ->
                line.contains("Сгенерирована подсказка:") ||
                        line.contains("Подсказка:")
        );

        assertTrue(found, "В логе нет сообщения о подсказке. Лог:\n" + String.join("\n", log));
    }

}
