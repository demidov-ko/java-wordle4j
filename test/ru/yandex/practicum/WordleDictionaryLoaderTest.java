package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.DictionaryLoadException;
import ru.yandex.practicum.exceptions.WordleIOException;
import ru.yandex.practicum.wordle_auxiliary_classes.GameLogger;
import ru.yandex.practicum.wordle_auxiliary_classes.WordleDictionary;
import ru.yandex.practicum.wordle_auxiliary_classes.WordleDictionaryLoader;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class WordleDictionaryLoaderTest {
    private WordleDictionaryLoader loader;
    private GameLogger logger;
    private PrintWriter printWriter;
    private StringWriter stringWriter;

    @BeforeEach
    void setUp() {
        // Буфер в памяти для логов
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
        // Создаём GameLogger с PrintWriter
        logger = new GameLogger(printWriter);
        // Передаём GameLogger в загрузчик
        loader = new WordleDictionaryLoader(logger);
    }

    @Test
    void testLoadFromFileValidFile() throws IOException {
        // Создаём временный файл с тестовыми словами
        Path tempFile = Files.createTempFile("test_words", ".txt");
        Files.write(tempFile, List.of("ТАКСИ", "СЕКТА", "ЗАБЕГ"));

        WordleDictionary dictionary = loader.loadFromFile(tempFile.toString());

        assertTrue(dictionary.contains("ТАКСИ"));
        assertTrue(dictionary.contains("СЕКТА"));
        assertTrue(dictionary.contains("ЗАБЕГ"));
        assertEquals(3, dictionary.size());

        // Проверяем логи: должно быть сообщение о загрузке
        String logOutput = stringWriter.toString();
        System.out.println(logOutput);
        assertTrue(logOutput.contains("[INFO] Загружено 3 слов из 5 букв из файла: " + tempFile));

        Files.delete(tempFile); // удаляем временный файл
    }

    @Test
    void testLoadFromFileNonExistentFile() {
        //assertThrows проверяет, что при выполнении кода выбрасывается ожидаемое исключение WordleIOException.class;
        //автоматически проваливает тест, если исключение не выброшено или выброшено другое исключение.
        //Второй параметр: лямбда‑выражение () -> loader.loadFromFile("non_existent.txt") — код, который должен вызвать исключение
        assertThrows(WordleIOException.class, () -> loader.loadFromFile("non_existent.txt"),
                "Ожидалось исключение WordleIOException при загрузке несуществующего файла");
        // Проверяем, что в логах есть сообщение об ошибке
        String logOutput = stringWriter.toString();
        System.out.println(logOutput);
        assertTrue(logOutput.contains("[ERROR] Ошибка загрузки словаря из файла non_existent.txt"));

    }

    @Test
    void testLoadFromFile_EmptyFile() throws IOException {
        Path tempFile = Files.createTempFile("empty", ".txt");
        assertThrows(DictionaryLoadException.class, () -> loader.loadFromFile(tempFile.toString()),
                "Ожидалось исключение DictionaryLoadException при загрузке пустого файла");
        // Проверяем логи
        String logOutput = stringWriter.toString();
        System.out.println(logOutput);
        assertTrue(logOutput.contains("[ERROR] Словарь пуст"),
                "В логах отсутствует сообщение об ошибке");

        Files.deleteIfExists(tempFile);
    }
}
