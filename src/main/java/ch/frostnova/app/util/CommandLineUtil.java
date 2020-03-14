package ch.frostnova.app.util;

import java.util.Scanner;
import java.util.regex.Pattern;

public final class CommandLineUtil {

    private final static String ANSI_RESET = "\u001b[0m";
    private final static String ANSI_BOLD = "\u001b[1m";

    private CommandLineUtil() {

    }

    /**
     * Prompt user to input a value
     *
     * @param message      message to show when prompting for input
     * @param defaultValue default value to display/use
     * @return value
     */
    public static String promptValue(String message, String defaultValue) {
        return promptValue(message, defaultValue, null);
    }

    /**
     * Prompt user to input a value
     * @param message message to show when prompting for input
     * @param defaultValue default value to display/use
     * @param pattern input format pattern for validation
     * @return value
     */
    public static String promptValue(String message, String defaultValue, Pattern pattern) {
        Scanner scanner = new Scanner(System.in);
        String input = null;
        while (input == null) {
            System.out.print(ANSI_BOLD + message + ANSI_RESET + (defaultValue != null ? " (" + defaultValue + "): " : ": "));
            input = scanner.nextLine().trim();
            if (input.trim().length() == 0) {
                input = defaultValue;
            }
            if (input != null && pattern != null && !pattern.matcher(input).matches()) {
                System.err.println("   [!] invalid format, expected: " + pattern.pattern());
                input = null;
            }
        }
        return input;
    }
}
