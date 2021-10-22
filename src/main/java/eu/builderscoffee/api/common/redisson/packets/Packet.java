package eu.builderscoffee.api.common.redisson.packets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.builderscoffee.api.common.redisson.Redis;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
public abstract class Packet {

    @Setter(AccessLevel.NONE)
    protected String serverName = Redis.getDefaultServerName();
    protected String packetId = UUID.randomUUID().toString();
    protected String targetServerName;
    @Setter(AccessLevel.NONE)
    private Date creationDate = new Date();

    /***
     * Cree un gson qui enregistre le type de class pour ne rien perde lors de la deserialisation
     * @return
     */
    protected final static Gson getGson(Class<? extends Packet> clazz) {
        return new GsonBuilder().registerTypeAdapter(clazz, new PacketAdapter()).create();
    }

    /***
     * Désérialiser un packet en Gson
     * @param json - Message à désérialiser
     * @param clazz - Class du packet
     * @param <T> - L'object dépendent de la classe du packet donnée
     * @return - Retourne l'object du packet
     */
    public final static <T extends Packet> T deserialize(String json, Class<T> clazz) {
        return getGson(clazz).fromJson(json, clazz);
    }

    /***
     * Désérialiser un packet en Gson
     * @param json - Message à désérialiser
     * @return - Retourne l'object du packet
     */
    public final static Packet deserialize(String json) {
        return getGson(Packet.class).fromJson(json, Packet.class);
    }

    /***
     * Sérialiser un packet en Gson
     * @return - Retourne le json
     */
    public final String serialize() {
        return getGson(getClass()).toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return Objects.equals(packetId, packet.packetId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(packetId);
    }

    @Override
    public String toString() {
        return serialize();
    }
}
