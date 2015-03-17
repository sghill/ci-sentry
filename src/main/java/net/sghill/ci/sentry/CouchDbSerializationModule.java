package net.sghill.ci.sentry;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;

public class CouchDbSerializationModule extends SimpleModule {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = ISODateTimeFormat.basicDateTime();

    public CouchDbSerializationModule() {
        super("couchdb-serialization", Version.unknownVersion());
        addDeserializer(DateTime.class, new JsonDeserializer<DateTime>() {
            @Override
            public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                return DateTime.parse(jsonParser.getText(), DATE_TIME_FORMATTER);
            }
        });
        addSerializer(DateTime.class, new JsonSerializer<DateTime>() {
            @Override
            public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(dateTime.toString(DATE_TIME_FORMATTER));
            }
        });
    }
}
