package eu.builderscoffee.api.common.redisson.listeners;

import eu.builderscoffee.api.common.redisson.packets.types.RequestPacket;
import eu.builderscoffee.api.common.redisson.packets.types.ResponsePacket;
import lombok.val;

import java.util.*;
import java.util.concurrent.TimeUnit;

public final class ResponseListener implements PacketListener {

    private static int maxTimeToLive = 30;
    public final HashMap<String, RequestPacket> requestedPackets = new HashMap<>();

    public ResponseListener() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                val now = new Date();
                val toRemove = new ArrayList<String>();
                requestedPackets.values().forEach(packet -> {
                    long diffInMillies = Math.abs(now.getTime() - packet.getCreationDate().getTime());
                    long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    if (diff > maxTimeToLive) {
                        toRemove.add(packet.getPacketId());
                    }
                });
                toRemove.forEach(requestedPackets::remove);
            }
        }, maxTimeToLive * 1000, maxTimeToLive * 1000);
    }

    public void anyResponsePacket(ResponsePacket packet){
        if (requestedPackets.containsKey(packet.getPacketId())) {
            if (requestedPackets.get(packet.getPacketId()).onResponse != null)
                requestedPackets.get(packet.getPacketId()).onResponse.invoke(packet);
            requestedPackets.remove(packet.getPacketId());
        }
    }
}
