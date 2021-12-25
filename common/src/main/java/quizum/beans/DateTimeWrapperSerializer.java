package quizum.beans;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class DateTimeWrapperSerializer extends JsonSerializer<DateTimeWrapper> {
    @Override
    public void serialize(DateTimeWrapper value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.toString());
    }
}
