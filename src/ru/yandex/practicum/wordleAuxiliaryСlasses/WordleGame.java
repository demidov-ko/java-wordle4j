package ru.yandex.practicum.wordleAuxiliaryСlasses;

import ru.yandex.practicum.exceptions.*;

import java.util.*;

public class WordleGame {

    private final Set<Character> absentLetters; //буквы, которых нет в слове
    private final Map<Integer, Character> correctLetters; //буквы на своих местах
    private final Map<Integer, Set<Character>> presentLetters; //буквы не на своих местах
    private final LinkedHashMap<String, Integer> wordFrequency; //счётчик вхождений каждого слова
    private static final Random RANDOM = new Random();

    private final String answer; //загаданное слово
    private int steps; //счётчик попыток
    private final WordleDictionary dictionary; //ссылка на словарь
    private final List<String> guesses = new ArrayList<>(); //список догадок
    public static final int MAX_ATTEMPTS = 6; //максимальное кол-во попыток
    private final GameLogger logger;

    public WordleGame(WordleDictionary dictionary, GameLogger logger) {
        this.answer = dictionary.getRandomWord();
        this.steps = 0;
        this.dictionary = dictionary;
        this.logger = logger;
        logger.info("Загадано слово: " + answer);

        this.absentLetters = new HashSet<>();
        this.correctLetters = new HashMap<>();
        this.presentLetters = new HashMap<>();
        this.wordFrequency = new LinkedHashMap<>();
    }

    //метод вывод подсказки
    public String makeGuess(String guess) throws WordleGameException {
        String guessTrim = guess.trim(); //догадка

        // если ввод пуст — выдаём подсказку
        if (guessTrim.isEmpty()) {
            return getHint();
        }
        normalizeAndValidate(guessTrim);
        steps++;
        guesses.add(guessTrim);

        String result = analyzeGuess(guessTrim);

        // проверяем наличие слова в словаре ПОСЛЕ анализа
        String upperGuess = guess.toUpperCase();
        if (!dictionary.contains(upperGuess)) {
            logger.warning("Слово '" + upperGuess + "' не найдено в словаре",
                    new WordNotFoundInDictionary(upperGuess));
            result += " (слово не в словаре)";
        }
        logger.info("Попытка " + steps + ": " + guessTrim + " → " + result);
        return result;
    }

    public void normalizeAndValidate(String guess) throws WordleGameException {
        // проверка длины
        if (guess.length() != 5) {
            throw new InvalidWordLength(guess.length(), 5);
        }
        String normalized = guess.toUpperCase();
        if (!normalized.matches("[А-Яа-яЁё]+")) {
            throw new WordleGameException("Слово должно содержать только русские буквы");
        }
    }

    private String analyzeGuess(String guess) {
        char[] target = answer.toCharArray();
        char[] guessChars = guess.toUpperCase().toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            char c = guessChars[i];
            if (c == target[i]) {
                sb.append(LetterStatus.CORRECT.symbol).append(" ");
                correctLetters.put(i, c); //фиксируем правильную букву на позиции
            } else if (answer.indexOf(c) != -1) {
                sb.append(LetterStatus.PRESENT.symbol).append(" ");
                presentLetters.computeIfAbsent(i, k -> new HashSet<>()).add(c); //фиксируем присутствующую букву
            } else {
                sb.append(LetterStatus.ABSENT.symbol).append(" ");
                absentLetters.add(c); //фиксируем отсутствующую букву
            }
        }
        // Обновляем частоту слова в статистике
        wordFrequency.merge(guess.toUpperCase(), 1, Integer::sum);
        return sb.toString().trim();
    }

    //метод генерирует подсказку
    public String getHint() {
        List<String> possibleWords = dictionary.getPossibleWords(absentLetters, correctLetters, presentLetters);

        if (possibleWords.isEmpty()) {
            logger.warning("Нет подходящих слов для подсказки", new WordleGameException("Нет доступных слов"));
            return "Нет подходящих слов";
        }

        // сортировка по частоте: сначала редкие слова (меньше вхождений)
        possibleWords.sort((w1, w2) -> {
            int freq1 = wordFrequency.getOrDefault(w1, 0);
            int freq2 = wordFrequency.getOrDefault(w2, 0);
            return Integer.compare(freq1, freq2);
        });

        String hint = null;
        int attempts = 0;
        final int MAX_HINT_ATTEMPTS = 100;

        do {
            hint = possibleWords.get(RANDOM.nextInt(possibleWords.size()));
            attempts++;
        } while (guesses.contains(hint) && attempts < MAX_HINT_ATTEMPTS);

        if (attempts >= MAX_HINT_ATTEMPTS) {
            logger.warning("Не удалось сгенерировать подсказку после " + MAX_HINT_ATTEMPTS +
                    " попыток", new WordleGameException("Нет доступных слов"));
            return "Не удалось сгенерировать подсказку";
        }

        logger.info("Сгенерирована подсказка: " + hint);
        return "Подсказка: " + hint;
    }

    //метод проверки победы
    public boolean isWon() {
        return !guesses.isEmpty() && guesses.get(guesses.size() - 1).equalsIgnoreCase(answer);
    }

    //метод проверки проигрыша
    public boolean isLost() {
        return steps >= MAX_ATTEMPTS && !isWon();
    }

    //метод возврата ост. попыток
    public int getRemainingAttempts() {
        return MAX_ATTEMPTS - steps;
    }

    //метод возвращает загаданное слово
    public String getTargetWord() {
        return answer;
    }

    //метод возвращает историю попыток
    public List<String> getGuesses() {
        return new ArrayList<>(guesses);
    }
}
