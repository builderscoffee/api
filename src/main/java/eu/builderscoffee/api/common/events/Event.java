package eu.builderscoffee.api.common.events;

import lombok.Getter;

@Getter
public abstract class Event {

    private boolean canceled = false;
}
