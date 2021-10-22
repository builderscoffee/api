package eu.builderscoffee.api.common.redisson.packets.types.common;

import eu.builderscoffee.api.common.redisson.packets.Packet;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class HeartBeatPacket extends Packet {

}
