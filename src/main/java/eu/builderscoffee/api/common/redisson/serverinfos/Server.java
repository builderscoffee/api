package eu.builderscoffee.api.common.redisson.serverinfos;

import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.RedisTopic;
import eu.builderscoffee.api.common.redisson.packets.types.playpen.actions.DeprovisionServerPacket;
import eu.builderscoffee.api.common.redisson.packets.types.playpen.actions.ProvisionServerPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.val;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@Getter
@Setter
public class Server implements Comparable<Server> {

    protected String hostName;
    protected String hostAddress;
    protected int hostPort;
    protected int playerCount;
    protected int playerMaximum;
    protected Date lastHeartbeat = new Date();
    protected ServerType serverType;
    protected ServerStatus serverStatus;
    protected ServerStartingMethod startingMethod;

    public enum ServerStatus{
        NONE,
        RUNNING,
        WAITING_PLAYERS,
        WAITING_INFO,
        PLAYING
    }

    public enum ServerStartingMethod{
        NONE,
        STATIC,
        DYNAMIC
    }

    public enum ServerType{
        NONE,
        SPIGOT,
        BUNGEECORD
    }

    public final void start(String packetId){
        val packet = new ProvisionServerPacket();
        packet.setNewServerPacketId(packetId);
        packet.setNewServerName(getHostName());
        val properties = new HashMap<String, String>();
        properties.put("ip", getHostAddress());
        properties.put("port", String.valueOf(getHostPort()));
        packet.setNewServerProperties(properties);

        Redis.publish(RedisTopic.PLAYPEN, packet);
    }

    public final void stop(){
        val packet = new DeprovisionServerPacket();
        packet.setTargetServerName(getHostName());

        Redis.publish(RedisTopic.PLAYPEN, packet);
    }

    @Override
    public int compareTo(Server o) {
        return getHostName().compareTo(o.getHostName());
    }

    @Override
    public String toString() {
        return hostName;
    }

    @Override
    public boolean equals(Object o) {
        System.out.println("Equals");
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server that = (Server) o;
        System.out.println("Equals: " + (hostPort == that.hostPort && Objects.equals(hostAddress, that.hostAddress)));
        return hostPort == that.hostPort && Objects.equals(hostAddress, that.hostAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hostAddress, hostPort);
    }
}
