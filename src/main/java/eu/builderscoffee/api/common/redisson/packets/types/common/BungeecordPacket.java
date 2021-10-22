package eu.builderscoffee.api.common.redisson.packets.types.common;

import eu.builderscoffee.api.common.redisson.packets.Packet;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class BungeecordPacket extends Packet {

    protected String hostName;
    protected String hostAddress;
    protected int hostPort;
    protected ServerStatus serverStatus;

    public enum ServerStatus{
        NONE,
        STARTED,
        STOPPED
    }
}
