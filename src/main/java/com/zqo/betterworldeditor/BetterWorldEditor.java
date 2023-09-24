package com.zqo.betterworldeditor;

import com.zqo.betterworldeditor.handlers.CommandHandler;
import com.zqo.betterworldeditor.handlers.ListenerHandler;
import org.bukkit.plugin.java.JavaPlugin;


public final class BetterWorldEditor extends JavaPlugin {
    private static BetterWorldEditor worldZqo;

    @Override
    public void onEnable()
    {
        worldZqo = this;

        new ListenerHandler().register();
        new CommandHandler().register();
    }

    public static BetterWorldEditor getWorldZqo() {
        return worldZqo;
    }
}
