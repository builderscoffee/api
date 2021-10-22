package eu.builderscoffee.api.bukkit;

import eu.builderscoffee.api.bukkit.bungeecord.BungeeChannelApi;
import eu.builderscoffee.api.bukkit.gui.InventoryManager;
import eu.builderscoffee.api.common.redisson.Redis;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private static InventoryManager invManager;

    @Getter
    private static BungeeChannelApi bungeeChannelApi;

    @Override
    public void onEnable() {
        instance = this;

        invManager = new InventoryManager(this);
        invManager.init();

        bungeeChannelApi = BungeeChannelApi.of(this);
    }

    @Override
    public void onDisable() {

    }
}
