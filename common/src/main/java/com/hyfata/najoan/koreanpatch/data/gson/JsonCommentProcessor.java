package com.hyfata.najoan.koreanpatch.data.gson;

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

    public void writeWithComments(Object obj, Writer writer) throws IOException {
        writeObject(obj, writer, 0, new IdentityHashMap<>());
    }

    public <T> T readRemovingComments(Reader reader, Class<T> type) throws IOException {
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

    private void writeObject(Object obj, Writer writer, int indent, Map<Object, Boolean> visited) throws IOException {
        if (obj == null) {
            writer.write("null");
            return;
        }

        if (isPrimitive(obj) || obj.getClass().isEnum() || isJavaClass(obj.getClass())) {
            gson.toJson(obj, writer);
            return;
        }

        if (visited.containsKey(obj)) {
            writer.write("\"<circular>\"");
            return;
        }
        visited.put(obj, true);

        Field[] fields = obj.getClass().getDeclaredFields();
        writer.write("{\n");

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);

            Object value;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                continue;
            }

            JsonComment comment = field.getAnnotation(JsonComment.class);
            if (comment != null) {
                indent(writer, indent + 1);

                StringBuilder commentLine = new StringBuilder("// " + comment.value());

                if (comment.enums() && field.getType().isEnum()) {
                    Object[] enumConstants = field.getType().getEnumConstants();
                    if (enumConstants != null) {
                        String enumList = Arrays.stream(enumConstants)
                                .map(Object::toString)
                                .collect(Collectors.joining(", "));
                        commentLine.append("[").append(enumList).append("]");
                    }
                }

                writer.write(commentLine.toString() + "\n");
            }

            indent(writer, indent + 1);
            writer.write("\"" + field.getName() + "\": ");
            writeObject(value, writer, indent + 1, visited);

            if (i < fields.length - 1) {
                writer.write(",");
            }
            writer.write("\n");
        }

        indent(writer, indent);
        writer.write("}");
        visited.remove(obj);
    }

    private boolean isPrimitive(Object value) {
        if (value == null) return true;
        Class<?> c = value.getClass();
        return c.isPrimitive() || c == String.class || Number.class.isAssignableFrom(c) || Boolean.class.isAssignableFrom(c);
    }

    private boolean isJavaClass(Class<?> clazz) {
        return clazz.getPackage() != null && clazz.getPackage().getName().startsWith("java");
    }

    private void indent(Writer writer, int indent) throws IOException {
        for (int i = 0; i < indent; i++) {
            writer.write("  ");
        }
    }
}
