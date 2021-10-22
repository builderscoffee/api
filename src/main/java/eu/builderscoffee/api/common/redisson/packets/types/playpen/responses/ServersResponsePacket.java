package eu.builderscoffee.api.common.redisson.packets.types.playpen.responses;


import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import eu.builderscoffee.api.common.redisson.packets.types.ResponsePacket;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ServersResponsePacket extends ResponsePacket {

    protected List<String> servers = new ArrayList<>();

    public ServersResponsePacket(String packetId) {
        super(packetId);
    }

    public ServersResponsePacket(RequestPacket requestPacket) {
        super(requestPacket);
    }
}
