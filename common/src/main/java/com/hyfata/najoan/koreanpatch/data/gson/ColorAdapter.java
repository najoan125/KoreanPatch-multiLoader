package com.hyfata.najoan.koreanpatch.data.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.awt.*;
import java.io.IOException;

public class ColorAdapter extends TypeAdapter<Color> {

    @Override
    public void write(JsonWriter out, Color color) throws IOException {
        if (color == null) {
            out.nullValue();
            return;
        }

        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        String hex = (a == 255)
                ? String.format("#%02X%02X%02X", r, g, b)
                : String.format("#%02X%02X%02X%02X", r, g, b, a);

        out.value(hex);
    }

    @Override
    public Color read(JsonReader in) throws IOException {
        JsonToken token = in.peek();

        if (token == JsonToken.NUMBER) {
            return new Color(in.nextInt());
        }

        if (token != JsonToken.STRING) {
            throw new IOException("Expected a hex string or int for color, but got: " + token);
        }

        String hex = in.nextString().replace("#", "");
        int length = hex.length();

        if (length != 6 && length != 8) {
            throw new IOException("Invalid hex color length: " + hex);
        }

        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        int a = (length == 8) ? Integer.parseInt(hex.substring(6, 8), 16) : 255;

        return new Color(r, g, b, a);
    }
}
