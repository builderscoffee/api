package eu.builderscoffee.api.common.redisson.packets;

import com.google.gson.*;

import java.lang.reflect.Type;

public final class PacketAdapter implements JsonSerializer<Packet>, JsonDeserializer<Packet> {
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();

    @Override
    public JsonElement serialize(Packet src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(src.getClass().getName()));
        result.add("properties", gson.toJsonTree(src));

        return result;
    }

    @Override
    public Packet deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");

        try {
            return (Packet) gson.fromJson(element, Class.forName(type));
        } catch (ClassNotFoundException cnfe) {
            throw new JsonParseException("Unknown element type: " + type, cnfe);
        }
    }
}
