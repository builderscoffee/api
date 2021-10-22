package eu.builderscoffee.api.common.utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogUtils {

    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static void info(String message) {
        log.info(message + RESET);
    }

    public static void debug(String message) {
        log.debug(CYAN + message + RESET);
    }

    public static void error(String message) {
        log.info(RED + message + RESET);
    }

    public static void warn(String message) {
        log.warn(YELLOW + message + RESET);
    }

    public static void fatal(String message) {
        log.fatal(RED + message + RESET);
    }
}
