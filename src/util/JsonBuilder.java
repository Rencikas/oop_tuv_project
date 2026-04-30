package util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Simple JSON builder for responses (without external dependencies)
 */
public class JsonBuilder {
    private Map<String, Object> data = new LinkedHashMap<>();

    public JsonBuilder put(String key, Object value) {
        data.put(key, value);
        return this;
    }

    public JsonBuilder put(String key, String value) {
        data.put(key, escapeString(value));
        return this;
    }

    public JsonBuilder put(String key, boolean value) {
        data.put(key, value);
        return this;
    }

    public JsonBuilder put(String key, int value) {
        data.put(key, value);
        return this;
    }

    public JsonBuilder putArray(String key, List<?> list) {
        data.put(key, list);
        return this;
    }

    @Override
    public String toString() {
        return mapToJson(data);
    }

    private static String mapToJson(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (!first)
                sb.append(",");
            sb.append("\"").append(entry.getKey()).append("\":");
            sb.append(valueToJson(entry.getValue()));
            first = false;
        }

        sb.append("}");
        return sb.toString();
    }

    private static String valueToJson(Object value) {
        if (value == null) {
            return "null";
        } else if (value instanceof String) {
            return "\"" + value.toString().replace("\"", "\\\"") + "\"";
        } else if (value instanceof Boolean) {
            return value.toString();
        } else if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof List) {
            List<?> list = (List<?>) value;
            return "[" + list.stream()
                    .map(JsonBuilder::valueToJson)
                    .collect(Collectors.joining(",")) + "]";
        } else if (value instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) value;
            return mapToJson(map);
        } else {
            return "\"" + value.toString().replace("\"", "\\\"") + "\"";
        }
    }

    private static String escapeString(String str) {
        if (str == null)
            return null;
        return str.replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Helper method to parse URL query parameters
    public static Map<String, String> parseQuery(String query) {
        Map<String, String> params = new HashMap<>();
        if (query != null && !query.isEmpty()) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    try {
                        params.put(pair[0], java.net.URLDecoder.decode(pair[1], "UTF-8"));
                    } catch (Exception e) {
                        params.put(pair[0], pair[1]);
                    }
                }
            }
        }
        return params;
    }
}
