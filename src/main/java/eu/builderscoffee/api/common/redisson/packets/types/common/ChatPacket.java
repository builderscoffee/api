package eu.builderscoffee.api.common.redisson.packets.types.common;

import eu.builderscoffee.api.common.redisson.packets.Packet;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public abstract class ChatPacket extends Packet {

    protected String playerName;
    protected String message;

}
