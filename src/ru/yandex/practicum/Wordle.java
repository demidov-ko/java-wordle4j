package ru.yandex.practicum;

import ru.yandex.practicum.exceptions.*;
import ru.yandex.practicum.wordleAuxiliaryСlasses.GameLogger;
import ru.yandex.practicum.wordleAuxiliaryСlasses.WordleDictionary;
import ru.yandex.practicum.wordleAuxiliaryСlasses.WordleDictionaryLoader;
import ru.yandex.practicum.wordleAuxiliaryСlasses.WordleGame;


import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "wordle.log";

    public static void main(String[] args) {
        GameLogger logger = null;
        WordleGame game = null;
        Scanner scanner = new Scanner(System.in);
        PrintWriter fileWriter = null;

        try {
            fileWriter = new PrintWriter(new FileWriter(LOG_FILE, false));
            logger = new GameLogger(fileWriter);
            logger.info("Запуск игры Wordle");

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logger);
            WordleDictionary dictionary = loader.loadFromFile(DICTIONARY_FILE);
            logger.info("Словарь загружен: " + dictionary.size() + " слов");

            game = new WordleGame(dictionary, logger);
            logger.info("Игра создана. Загадано слово.");

            System.out.println("=".repeat(50));
            System.out.println("             ДОБРО ПОЖАЛОВАТЬ В WORDLE!");
            System.out.println("=".repeat(50));
            System.out.println("Правила:");
            System.out.println("- У вас " + WordleGame.MAX_ATTEMPTS + " попыток, чтобы угадать слово из 5 букв.");
            System.out.println("- Введите слово и нажмите Enter.");
            System.out.println("- Если введёте пустую строку — получите подсказку.");
            System.out.println("- Обозначения:");
            System.out.println("  \uD83D\uDFE9 — буква на своём месте");
            System.out.println("  \uD83D\uDFE8 — буква не на своем месте");
            System.out.println("  \uD83D\uDFE5 — буквы нет в слове");
            System.out.println("-".repeat(50));

            while (true) {
                System.out.print("\nВаш ход (" + game.getRemainingAttempts() + " попыток): ");
                String input = scanner.nextLine().trim();

                try {
                    String result = game.makeGuess(input);

                    System.out.println("-".repeat(40));
                    if (result.startsWith("Подсказка:")) {
                        System.out.println(result);
                    } else {
                        System.out.println("Результат: " + result);
                    }
                    System.out.println("-".repeat(40));

                    if (game.isWon()) {
                        System.out.println("ПОЗДРАВЛЯЕМ! Вы угадали слово: " + game.getTargetWord());
                        logger.info("Игрок угадал слово: " + game.getTargetWord());
                        break;
                    }

                    if (game.isLost()) {
                        System.out.println("Игра окончена! Вы использовали все попытки.");
                        System.out.println("Загаданное слово было: " + game.getTargetWord());
                        logger.info("Игрок не угадал слово. Загаданное: " + game.getTargetWord());
                        break;
                    }

                } catch (WordNotFoundInDictionary e) {
                    System.out.println("Ошибка: слово '" + input + "' не найдено в словаре.");
                    logger.warning("Неверное слово: " + input, e);
                } catch (InvalidWordLength e) {
                    System.out.println("Ошибка: длина слова должна быть 5 букв (введено: " + input.length() + ").");
                    logger.warning("Неверная длина слова: " + input, e);
                } catch (MaxAttemptsExceeded e) {
                    System.out.println("Ошибка: превышено число попыток.");
                    logger.warning("Превышено число попыток", e);
                    break;
                } catch (WordleGameException e) {
                    System.out.println("Игровая ошибка: " + e.getMessage());
                    logger.warning("Игровая ошибка", e);
                } catch (Exception e) {
                    System.out.println("Непредвиденная ошибка: " + e.getMessage());
                    logger.error("Непредвиденная ошибка в игровом цикле", e);
                }
            }


        } catch (DictionaryLoadException e) {
            System.err.println("Критическая ошибка: не удалось загрузить словарь. Проверьте файл '" +
                    DICTIONARY_FILE + "'");
            if (logger != null) logger.error("Ошибка загрузки словаря", e);
        } catch (WordleIOException e) {
            System.err.println("Критическая ошибка: не удалось создать лог‑файл '" + LOG_FILE + "'");
            if (logger != null) logger.error("Ошибка инициализации логгера", e);
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
            if (logger != null) logger.error("Непредвиденная ошибка в главном цикле", e);
        } finally {
            if (scanner != null) scanner.close();
            if (logger != null) logger.close();
            System.out.println("\n" + "=".repeat(50));
            System.out.println("Игра завершена. Спасибо за игру!");
            System.out.println("=".repeat(50));
        }
    }
}

/*С Новым Годом, Давид! Пускай работа будет в кайф и сильно не напрягает!
Что-то не пошел у меня нормально блок тестирования до нг, вот только сейчас решил вернуться к нему) */
