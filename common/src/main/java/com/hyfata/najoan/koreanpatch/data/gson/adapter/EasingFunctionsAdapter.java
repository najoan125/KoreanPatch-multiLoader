package com.hyfata.najoan.koreanpatch.data.gson.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.hyfata.najoan.koreanpatch.data.provider.EasingFunctions;

import java.io.IOException;

public class EasingFunctionsAdapter extends TypeAdapter<EasingFunctions> {
    @Override
    public void write(JsonWriter out, EasingFunctions value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.value(value.name());
    }

    @Override
    public EasingFunctions read(JsonReader in) throws IOException {
        String name = in.nextString();
        return EasingFunctions.valueOf(name);
    }
}
