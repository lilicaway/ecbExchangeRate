package com.liliana.sample.exchange.model.serializer;

import java.io.IOException;
import java.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateIso8601Deserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException,
            JsonProcessingException {
        return LocalDate.parse(jsonParser.getText());
    }
}