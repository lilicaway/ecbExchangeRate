package com.liliana.sample.exchange.model.serializer;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalDateIso8601Serializer extends JsonSerializer<LocalDate> {
    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider provider)
            throws IOException,
            JsonProcessingException {
        jsonGenerator.writeString(localDate.toString());
    }
}