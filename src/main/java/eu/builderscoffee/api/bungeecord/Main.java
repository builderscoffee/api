package eu.builderscoffee.api.bungeecord;

import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {

    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        // Nothing to do here
    }

    @Override
    public void onDisable() {
        // Nothing to do here
    }

}
