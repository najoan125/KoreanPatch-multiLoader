package com.hyfata.najoan.koreanpatch.config.gson;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonCommentProcessor {
    private final Gson gson;

    public JsonCommentProcessor(Gson gson) {
        this.gson = gson;
    }

    public <T> T readWithoutComments(Reader reader, Class<T> type) throws IOException {
        StringBuilder builder = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            builder.append((char) ch);
        }
        String cleaned = stripComments(builder.toString());
        return gson.fromJson(cleaned, type);
    }

    public static String stripComments(String json) {
        json = json.replaceAll("(?m)^\\s*//.*?$", "");
        json = json.replaceAll("(?s)/\\*.*?\\*/", "");
        return json;
    }

    public void writeWithComments(Object obj, Writer writer) throws IOException {
        writeObjectWithComments(obj, writer, 0, new IdentityHashMap<>());
    }

    private void writeObjectWithComments(Object obj, Writer writer, int indent, Map<Object, Boolean> visited) throws IOException {
        if (handleSpecialCases(obj, writer, visited)) return;

        writer.write("{\n");

        int currentIndent = indent + 1;
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = getAccessibleField(fields[i]);
            Object value;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                continue;
            }

            // write entry
            writeComment(writer, currentIndent, field);
            write(writer, currentIndent, "\"" + field.getName() + "\": ");
            writeObjectWithComments(value, writer, currentIndent, visited);

            if (i < fields.length - 1) {
                writer.write(",");
            }
            writer.write("\n");
        }

        write(writer, indent, "}");
        visited.remove(obj);
    }

    private boolean handleSpecialCases(Object obj, Writer writer, Map<Object, Boolean> visited) throws IOException {
        if (obj == null) {
            writer.write("null");
            return true;
        }

        if (isPrimitive(obj) || obj.getClass().isEnum() || isJavaClass(obj.getClass())) {
            gson.toJson(obj, writer);
            return true;
        }

        if (visited.containsKey(obj)) {
            writer.write("\"<circular>\"");
            return true;
        }
        visited.put(obj, true);
        return false;
    }

    private void writeComment(Writer writer, int indent, Field field) throws IOException {
        JsonComment comment = field.getAnnotation(JsonComment.class);
        if (comment != null) {
            writeIndent(writer, indent);

            StringBuilder commentLine = new StringBuilder("// " + comment.value());

            // enum
            if (comment.enums() && field.getType().isEnum()) {
                Object[] enumConstants = field.getType().getEnumConstants();
                if (enumConstants != null) {
                    String enumList = Arrays.stream(enumConstants)
                            .map(Object::toString)
                            .collect(Collectors.joining(", "));
                    commentLine.append("[").append(enumList).append("]");
                }
            }

            writer.write(commentLine + "\n");
        }
    }

    private boolean isPrimitive(Object value) {
        if (value == null) return true;
        Class<?> c = value.getClass();
        return c.isPrimitive() || c == String.class || Number.class.isAssignableFrom(c) || Boolean.class.isAssignableFrom(c);
    }

    private boolean isJavaClass(Class<?> clazz) {
        return clazz.getPackage() != null && clazz.getPackage().getName().startsWith("java");
    }

    private void writeIndent(Writer writer, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            writer.write("  ");
        }
    }

    private void write(Writer writer, int indent, String str) throws IOException {
        writeIndent(writer, indent);
        writer.write(str);
    }

    private Field getAccessibleField(Field field) {
        field.setAccessible(true);
        return field;
    }
}
