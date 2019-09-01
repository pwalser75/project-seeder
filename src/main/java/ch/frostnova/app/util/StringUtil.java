package ch.frostnova.app.util;

import java.util.Map;

public final class StringUtil {

    private StringUtil(){
    }

    /**
     * Replace all placeholders (format: ${key})in a given string with replacements in the provided map (key/replace-with)-
     * @param s input string
     * @param replacements replacements
     * @return resulting string
     */
    public static String replaceAll(String s, final Map<String, String> replacements) {
        if (s == null || replacements==null) {
            return s;
        }
        for (final String key : replacements.keySet()) {
            s = s.replace("${" + key + "}", replacements.get(key));
        }
        return s;
    }
}
