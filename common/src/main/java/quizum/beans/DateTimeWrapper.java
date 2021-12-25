package quizum.beans;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_TIME;
import static java.time.temporal.ChronoField.*;

@JsonSerialize(using = DateTimeWrapperSerializer.class)
@JsonDeserialize(using = DateTimeWrapperDeserializer.class)
public class DateTimeWrapper{
    private LocalDateTime dateTime;

    public DateTimeWrapper() {
        this.dateTime = LocalDateTime.now();
    }

    public DateTimeWrapper(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public static DateTimeWrapper now() {
        return new DateTimeWrapper();
    }

    @Override
    public String toString() {
        return dateTime.format(getFormatter());
    }

    protected static DateTimeFormatter getFormatter() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()
                .append(ISO_LOCAL_DATE)
                .appendLiteral(' ')
                .appendValue(HOUR_OF_DAY, 2)
                .appendLiteral(':')
                .appendValue(MINUTE_OF_HOUR, 2)
                .optionalStart()
                .appendLiteral(':')
                .appendValue(SECOND_OF_MINUTE, 2)
                .toFormatter();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
