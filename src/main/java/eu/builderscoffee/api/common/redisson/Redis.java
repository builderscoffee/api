package eu.builderscoffee.api.common.redisson;

import eu.builderscoffee.api.common.redisson.listeners.PubSubListener;
import eu.builderscoffee.api.common.redisson.listeners.ResponseListener;
import eu.builderscoffee.api.common.redisson.packets.Packet;
import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.util.HashMap;
import java.util.HashSet;

public class Redis {


    @Getter private static String defaultServerName;
    private static final HashSet<RedisTopic> topics = new HashSet<>();
    private static final HashMap<RedisTopic, ResponseListener> topicsWithResponseListener = new HashMap<>();
    public static RedissonClient redissonClient;

    /***
     * Initialiser redisson pour la connexion
     * @param credentials - Identifiants au network
     * @param threadNumber - Nombres de thread
     * @param nettyThreadsNumber - Numéro du thread netty
     */
    public static void Initialize(@NonNull String serverName, @NonNull RedisCredentials credentials, int threadNumber, int nettyThreadsNumber) {
        defaultServerName = serverName;

        val config = new Config()
                .setCodec(new JsonJacksonCodec())
                .setThreads(threadNumber)
                .setNettyThreads(nettyThreadsNumber);
        config.useSingleServer()
                .setAddress(credentials.toRedisUrl())
                .setPassword(credentials.getPassword())
                .setClientName(credentials.getClientName());

        redissonClient = Redisson.create(config);
    }

    /***
     * Ferme la connexion aux client
     */
    public static void close() {
        redissonClient.shutdown();
    }

    /***
     * S'abonner à un topic
     * @param topic - Channel de message
     * @param listener - Message listener
     */
    public static void subscribe(@NonNull RedisTopic topic, @NonNull PubSubListener listener) {
        RTopic rTopic = redissonClient.getTopic(topic.getName());
        rTopic.addListener(String.class, (channel, msg) -> listener.onMessage(msg));
        topics.add(topic);
    }

    /***
     * Se désabonner d'un topic
     * @param topic - Channel de message
     */
    public static void unsubscribe(@NonNull RedisTopic topic) {
        RTopic rtopic = redissonClient.getTopic(topic.getName());
        rtopic.removeAllListeners();
        if (topicsWithResponseListener.containsKey(topic)) topicsWithResponseListener.remove(topic);
    }

    public static void publish(RedisTopic topic, Packet packet) {
        if (packet instanceof RequestPacket) {
            val rPacket = (RequestPacket) packet;
            if (!topicsWithResponseListener.keySet().contains(topic)) {
                val listener = new ResponseListener();
                topicsWithResponseListener.put(topic, listener);
                subscribe(topic, listener);
            }
            topicsWithResponseListener.get(topic).requestedPackets.put(rPacket.getPacketId(), rPacket);
        }
        redissonClient.getTopic(topic.getName()).publish(packet.serialize());
    }

    /***
     * Se désabonner de tout les topics
     */
    public void unsubscribeAll() {
        topics.forEach(Redis::unsubscribe);
    }
}
