package eu.builderscoffee.api.common.redisson.packets.types.redisson.requests;

import eu.builderscoffee.api.common.redisson.packets.types.redisson.RedissonRequestPacket;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class RequestServerInfoPacket extends RedissonRequestPacket {

    // TODO Find other variable name
    protected String newServerName;
}
