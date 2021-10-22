package eu.builderscoffee.api.common.redisson.packets.types;

@FunctionalInterface
public interface IResponse {
    void invoke(ResponsePacket responsePacket);
}
