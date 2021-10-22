package eu.builderscoffee.api.common.redisson.packets.types.playpen.actions;

import eu.builderscoffee.api.common.redisson.packets.types.ActionPacket;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ProvisionServerPacket extends ActionPacket {

    protected String newServerName;
    protected String newServerPacketId;
    protected Map<String, String> newServerProperties = new HashMap<>();
}
