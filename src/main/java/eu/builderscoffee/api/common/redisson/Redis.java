package eu.builderscoffee.api.common.redisson;

import eu.builderscoffee.api.common.redisson.listeners.PacketListener;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.api.common.redisson.listeners.ResponseListener;
import eu.builderscoffee.api.common.redisson.packets.Packet;
import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.val;
import org.redisson.Redisson;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;

@UtilityClass
public class Redis {


    private static final HashSet<RedisTopic> topics = new HashSet<>();
    private static final HashMap<RedisTopic, ResponseListener> topicsWithResponseListener = new HashMap<>();
    @Getter private static RedissonClient redissonClient;
    @Getter private static String defaultServerName;

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
    public static void subscribe(@NonNull RedisTopic topic, @NonNull PacketListener listener) {
        // Récupère le topic
        RTopic rTopic = redissonClient.getTopic(topic.getName());
        // Ajoute un listener sur le topic
        rTopic.addListener(String.class, (channel, msg) -> {
            // Deserialise le packet
            val packet = Packet.deserialize(msg);
            // Loop toutes les fonctions du packetlistener
            for (Method method : listener.getClass().getDeclaredMethods()) {
                // Check si une des fonctions correspond au packet envoyé
                if(method.isAnnotationPresent(ProcessPacket.class)
                        && method.getParameterTypes().length == 1
                        && method.getParameterTypes()[0].isAssignableFrom(packet.getClass())){
                    try {
                        // Invoke la fonction en donnant le packet
                        method.invoke(listener, packet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
        // Check si c'est une demande qui attend une réponse
        if (packet instanceof RequestPacket) {
            val rPacket = (RequestPacket) packet;
            // Check si ce topic à déja un listener pour les réponses
            if (!topicsWithResponseListener.keySet().contains(topic)) {
                // Creer un listener
                val listener = new ResponseListener();
                // Ajoute le listener dans une map
                topicsWithResponseListener.put(topic, listener);
                // Ajoute le listener à redisson
                subscribe(topic, listener);
            }
            // Ajoute le packet dans une map en attente d'une réponse
            topicsWithResponseListener.get(topic).requestedPackets.put(rPacket.getPacketId(), rPacket);
        }
        // Envoi le packet dans le topic
        redissonClient.getTopic(topic.getName()).publish(packet.serialize());
    }

    /***
     * Se désabonner de tout les topics
     */
    public void unsubscribeAll() {
        topics.forEach(Redis::unsubscribe);
    }
}
