package com.hyfata.najoan.koreanpatch.config.gson.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.hyfata.najoan.koreanpatch.config.OutlineType;

import java.io.IOException;

public class OutlineTypeAdapter extends TypeAdapter<OutlineType> {
    @Override
    public void write(JsonWriter out, OutlineType value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.name());
    }

    @Override
    public OutlineType read(JsonReader in) throws IOException {
        String name = in.nextString();
        return OutlineType.valueOf(name);
    }
}
