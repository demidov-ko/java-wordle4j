package ru.yandex.practicum.wordle_auxiliary_classes;

import ru.yandex.practicum.exceptions.*;

import java.io.PrintWriter;

public class GameLogger implements AutoCloseable {
    private final PrintWriter logger;

    public GameLogger(PrintWriter writer) {

        this.logger = writer;
    }

    public void info(String message) {
        logger.println("[INFO] " + message);
    }

    public void warning(String message, WordleGameException e) {
        logger.println("[WARNING] " + message);
        e.printStackTrace(logger);
    }

    public void error(String message, Throwable e) {
        logger.println("[ERROR] " + message);
        e.printStackTrace(logger);
    }

    @Override
    public void close() {
        logger.close();
    }
}
