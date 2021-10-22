package eu.builderscoffee.api.common.redisson.packets.types;

import eu.builderscoffee.api.common.redisson.packets.Packet;

public abstract class RequestPacket extends Packet {

    public transient IResponse onResponse;
}
