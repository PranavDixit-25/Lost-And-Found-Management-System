package util;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for manual JSON parsing.
 * Since we cannot use external libraries like Gson,
 * this class provides basic JSON parsing for our data types.
 *
 * This handles simple flat JSON objects (no nested arrays).
 */
public class JsonParser {

    /**
     * Parse a JSON object string into a key-value Map.
     * Example input: {"name":"John","age":25,"city":"Delhi"}
     * Returns: {name=John, age=25, city=Delhi}
     */
    public static Map<String, String> parseObject(String json) {
        Map<String, String> result = new HashMap<>();

        if (json == null || json.trim().isEmpty()) {
            return result;
        }

        // Remove outer braces { }
        json = json.trim();
        if (json.startsWith("{")) json = json.substring(1);
        if (json.endsWith("}")) json = json.substring(0, json.length() - 1);

        // Split by comma, but be careful of commas inside string values
        // We parse character by character for accuracy
        StringBuilder currentKey = new StringBuilder();
        StringBuilder currentValue = new StringBuilder();
        boolean inString = false;
        boolean parsingKey = true;
        boolean escaped = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (escaped) {
                if (parsingKey) currentKey.append(c);
                else currentValue.append(c);
                escaped = false;
                continue;
            }

            if (c == '\\') {
                escaped = true;
                if (!parsingKey) currentValue.append(c);
                continue;
            }

            if (c == '"') {
                inString = !inString;
                continue;
            }

            if (!inString) {
                if (c == ':' && parsingKey) {
                    parsingKey = false;
                    continue;
                }
                if (c == ',') {
                    // Save the key-value pair
                    String key = currentKey.toString().trim();
                    String value = currentValue.toString().trim();
                    if (!key.isEmpty()) {
                        result.put(key, value);
                    }
                    currentKey = new StringBuilder();
                    currentValue = new StringBuilder();
                    parsingKey = true;
                    continue;
                }
            }

            if (parsingKey) currentKey.append(c);
            else currentValue.append(c);
        }

        // Don't forget the last key-value pair
        String key = currentKey.toString().trim();
        String value = currentValue.toString().trim();
        if (!key.isEmpty()) {
            result.put(key, value);
        }

        return result;
    }

    /**
     * Split a JSON array string into individual JSON object strings.
     * Example input: [{"id":"1"},{"id":"2"}]
     * Returns: [{"id":"1"}, {"id":"2"}]
     */
    public static java.util.List<String> parseArray(String json) {
        java.util.List<String> result = new java.util.ArrayList<>();

        if (json == null || json.trim().isEmpty()) return result;

        json = json.trim();
        if (json.startsWith("[")) json = json.substring(1);
        if (json.endsWith("]")) json = json.substring(0, json.length() - 1);
        json = json.trim();

        if (json.isEmpty()) return result;

        // Split individual objects by tracking brace depth
        int depth = 0;
        int start = 0;
        boolean inString = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
            }

            if (!inString) {
                if (c == '{') {
                    if (depth == 0) start = i;
                    depth++;
                } else if (c == '}') {
                    depth--;
                    if (depth == 0) {
                        result.add(json.substring(start, i + 1));
                    }
                }
            }
        }

        return result;
    }

    /**
     * Get an integer value from the parsed map with a default.
     */
    public static int getInt(Map<String, String> map, String key, int defaultValue) {
        String value = map.get(key);
        if (value == null) return defaultValue;
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * Get a string value from the parsed map with a default.
     */
    public static String getString(Map<String, String> map, String key, String defaultValue) {
        String value = map.get(key);
        return (value != null) ? value : defaultValue;
    }
}
