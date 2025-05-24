package ru.nstu.core.objects.util;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.awt.Color;
import java.io.IOException;

public class ColorTypeAdapter extends TypeAdapter<Color> {
    @Override
    public void write(JsonWriter jsonWriter, Color color) throws IOException {
        if (color == null) {
            jsonWriter.nullValue();
            return;
        }

        jsonWriter.value(color.getRGB());
    }

    @Override
    public Color read(JsonReader jsonReader) throws IOException {
        int rgb = jsonReader.nextInt();
        return new Color(rgb, true);
    }
}
