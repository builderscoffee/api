package eu.builderscoffee.api.common.redisson.packets.types.playpen.responses;

import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import eu.builderscoffee.api.common.redisson.packets.types.ResponsePacket;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class PackagesResponsePacket extends ResponsePacket {

    protected ArrayList<String> packages = new ArrayList<>();

    public PackagesResponsePacket(String packetId) {
        super(packetId);
    }

    public PackagesResponsePacket(RequestPacket requestPacket) {
        super(requestPacket);
    }
}
