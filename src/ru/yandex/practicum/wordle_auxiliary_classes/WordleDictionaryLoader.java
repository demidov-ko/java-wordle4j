package ru.yandex.practicum.wordle_auxiliary_classes;

import ru.yandex.practicum.exceptions.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class WordleDictionaryLoader {

    private final GameLogger logger;

    public WordleDictionaryLoader(GameLogger logger) {
        this.logger = logger;
    }

    //метод считывает слова из файла
    public WordleDictionary loadFromFile(String filename) {
        List<String> words = new ArrayList<>();
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(filename), "UTF-8")) {
            BufferedReader br = new BufferedReader(reader);
            String line;
            while ((line = br.readLine()) != null) {
                String word = line.toUpperCase().trim();
                if (word.length() == 5) {
                    words.add(word);
                }
            }
            words.replaceAll(s -> s.replace("ё", "е"));
        } catch (IOException e) {
            logger.error("Ошибка загрузки словаря из файла " + filename, e);
            throw new DictionaryLoadException("Не удалось загрузить словарь", e);
        }
        if (words.isEmpty()) {
            //создаем исключение и передаём его в логгер
            DictionaryLoadException emptyDictException = new DictionaryLoadException("Словарь пуст", null);
            logger.error("Словарь пуст", emptyDictException);
            throw emptyDictException;
        }
        logger.info("Загружено " + words.size() + " слов из 5 букв из файла: " + filename);
        return new WordleDictionary(words);
    }
}
