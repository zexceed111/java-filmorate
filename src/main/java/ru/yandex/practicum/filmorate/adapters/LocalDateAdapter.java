package ru.yandex.practicum.filmorate.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {
    static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void write(JsonWriter jsonWriter, LocalDate localDate) throws IOException {
        if (localDate == null)
            jsonWriter.value("null");
        else
            jsonWriter.value(localDate.format(dateTimeFormatter));
    }

    @Override
    public LocalDate read(JsonReader jsonReader) throws IOException {
        try {
            return LocalDate.parse(jsonReader.nextString(), dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new IOException("Invalid date format. Expected format: " + dateTimeFormatter.toString(), e);
        }
    }
}