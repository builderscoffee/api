package eu.builderscoffee.api.common.redisson.packets.types.playpen.responses;

import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import eu.builderscoffee.api.common.redisson.packets.types.ResponsePacket;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServerInfoResponsePacket extends ResponsePacket {

    // TODO Add more server information
    protected ServerStatus serverStatus = ServerStatus.UNKNOWN;
    protected ServerType serverType = ServerType.NONE;

    public ServerInfoResponsePacket(String packetId) {
        super(packetId);
    }

    public ServerInfoResponsePacket(RequestPacket requestPacket) {
        super(requestPacket);
    }

    private enum ServerStatus {
        UNKNOWN,
        RUNNING,
        FREEZED
    }

    public enum ServerType {
        NONE,
        PROXY,
        BUKKIT
    }
}
