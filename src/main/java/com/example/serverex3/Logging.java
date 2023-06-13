package com.example.serverex3;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.logging.LogLevel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logging {
    private static final String LOG_FOLDER = "Logs";
    private static final Logger LOGGER = LogManager.getLogger(Logging.class);
    private static final Logger TODO_LOGGER = LogManager.getLogger("todo-logger");

    public static void createLogFolder() {
        File logsFolder = new File(LOG_FOLDER);

        if (logsFolder.exists()) {
            LOGGER.info("Logs folder already exists.");
        } else {
            if (logsFolder.mkdir()) {
                LOGGER.info("Logs folder created successfully.");
            } else {
                LOGGER.info("Failed to create Logs folder.");
            }
        }
    }

    public static void logRequestCounter() {
        RequestCounter.incrementRequestNumber();
        int requestNumber = RequestCounter.getRequestNumber();
        LOGGER.info("Incoming request | #" + requestNumber + " | resource: {resource name} | HTTP Verb {HTTP VERB in capital letter (GET, POST, etc)}");
    }


    public static void logRequestDuration(long duration) {
        int requestNumber = RequestCounter.getRequestNumber();
        LOGGER.debug("request #" + requestNumber + " duration: " + duration + "ms");
    }

    public static void logTodoCreation(int todoId) {
        LOGGER.info("TODO created with id #" + todoId);
    }

    public static void logTodoUpdate(int todoId, String newStatus) {
        LOGGER.debug("TODO with id #" + todoId + " updated. New status: " + newStatus);
    }

    public static void logTodoDeletion(int todoId) {
        LOGGER.debug("TODO with id #" + todoId + " deleted");
    }

    public static void logLogLevelChange(String loggerName, LogLevel newLevel) {
        LOGGER.info("Log level changed for logger '" + loggerName + "'. New level: " + newLevel);
    }

    private static String generateLogEntry(LogLevel level, String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss.SSS");
        String dateTime = dateFormat.format(new Date());

        return String.format("%s %s: %s", dateTime, level.name(), message);
    }

    private static void writeLogToFile(String logEntry, String fileName) {
        File logFile = new File(LOG_FOLDER, fileName);

        try (FileWriter writer = new FileWriter(logFile, true)) {
            writer.write(logEntry + System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Error writing log entry to file: " + e.getMessage());
        }
    }
    public static void logTodoCreation(String title) {
        TODO_LOGGER.info("Creating new TODO with Title [" + title + "]");
    }

    public static void logTotalExistingTodos(int totalExistingTodos, int newTodoId) {
        TODO_LOGGER.debug("Currently there are " + totalExistingTodos + " TODOs in the system. New TODO will be assigned with id " + newTodoId);
    }

    public static void logGetTodoCount(String state, int totalTodos) {
        TODO_LOGGER.info("Total TODOs count for state " + state + " is " + totalTodos);
    }

    public static void logGetTodoData(String filter, String sorting) {
        TODO_LOGGER.info("Extracting todos content. Filter: " + filter + " | Sorting by: " + sorting);
    }

    public static void logTotalTodosCount(int totalTodos, int totalReturnedTodos) {
        TODO_LOGGER.debug("There are a total of " + totalTodos + " todos in the system. The result holds " + totalReturnedTodos + " todos");
    }

    public static void logUpdateTodoStatus(int todoId, String requestedState) {
        TODO_LOGGER.info("Update TODO id [" + todoId + "] state to " + requestedState);
    }

    public static void logTodoStateChange(int todoId, String oldState, String newState) {
        TODO_LOGGER.debug("Todo id [" + todoId + "] state change: " + oldState + " --> " + newState);
    }

    public static void logRemoveTodoById(int todoId) {
        TODO_LOGGER.info("Removing todo id " + todoId);
    }

    public static void logTotalRemainingTodos(int todoId, int totalRemainingTodos) {
        TODO_LOGGER.debug("After removing todo id [" + todoId + "] there are " + totalRemainingTodos + " TODOs in the system");
    }

}

