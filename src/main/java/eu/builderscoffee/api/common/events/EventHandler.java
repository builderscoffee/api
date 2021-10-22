package eu.builderscoffee.api.common.events;

import eu.builderscoffee.api.common.events.events.HeartBeatEvent;
import eu.builderscoffee.api.common.redisson.Redis;
import eu.builderscoffee.api.common.redisson.listeners.ProcessPacket;
import eu.builderscoffee.api.common.redisson.serverinfos.Server;
import lombok.NonNull;
import lombok.val;
import org.bukkit.Bukkit;
import org.redisson.api.RSortedSet;

import java.lang.reflect.Method;
import java.util.ArrayList;

public final class EventHandler {

    /**
     * Singleton
     */
    private EventHandler(){}
    private static class EventHandlerHolder {
        private static final EventHandler INSTANCE = new EventHandler();
    }
    public static EventHandler getInstance() {
        return EventHandlerHolder.INSTANCE;
    }

    /**
     * EventHandler
     */
    private ArrayList<EventListener> listeners = new ArrayList<>();

    public void callEvent(@NonNull Event event) {
        listeners.forEach(listener -> {
            for (Method method : listener.getClass().getDeclaredMethods()) {
                // Check si une des fonctions correspond à l'event envoyé
                if(method.isAnnotationPresent(ProcessEvent.class)
                        && method.getParameterTypes().length == 1
                        && method.getParameterTypes()[0].isAssignableFrom(event.getClass())){
                    try {
                        // Invoke la fonction en donnant l'event
                        method.invoke(listener, event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public boolean addListener(@NonNull EventListener eventListener) {
        return listeners.add(eventListener);
    }

    public boolean removeListener(@NonNull EventListener eventListener) {
        return listeners.remove(eventListener);
    }
}
