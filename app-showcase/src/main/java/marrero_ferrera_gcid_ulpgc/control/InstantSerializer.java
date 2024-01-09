package marrero_ferrera_gcid_ulpgc.control;

import com.google.gson.*;

import java.time.Instant;

public class InstantSerializer implements JsonSerializer<Instant>, JsonDeserializer<Instant> {
    @Override
    public JsonElement serialize(Instant instant, java.lang.reflect.Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(instant.toEpochMilli());
    }

    @Override
    public Instant deserialize(JsonElement jsonElement, java.lang.reflect.Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Instant.ofEpochMilli(jsonElement.getAsLong());
    }
}
