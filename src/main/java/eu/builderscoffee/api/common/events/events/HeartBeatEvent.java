package eu.builderscoffee.api.common.events.events;

import eu.builderscoffee.api.common.events.Event;
import eu.builderscoffee.api.common.redisson.serverinfos.Server;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public final class HeartBeatEvent extends Event {

    @NonNull private Server server;

    public HeartBeatEvent(@NonNull Server server) {
        this.server = server;
    }
}
