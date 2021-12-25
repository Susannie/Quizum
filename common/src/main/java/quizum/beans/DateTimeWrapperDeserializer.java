package quizum.beans;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.temporal.ChronoField.NANO_OF_SECOND;

public class DateTimeWrapperDeserializer extends JsonDeserializer<DateTimeWrapper> {
    @Override
    public DateTimeWrapper deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .append(DateTimeWrapper.getFormatter())
                .optionalStart()
                .appendFraction(NANO_OF_SECOND, 0, 9, true)
                .toFormatter();
        return new DateTimeWrapper(LocalDateTime.parse(p.getText(), formatter));
    }
}
