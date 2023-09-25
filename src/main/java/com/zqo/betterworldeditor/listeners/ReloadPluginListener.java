package com.zqo.betterworldeditor.listeners;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.Map;

public final class ReloadPluginListener implements Listener {
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<Player, SelectionManager> selectionManager = betterWorldEditor.getSelectionManagerMap();

    @EventHandler
    public void onJoin(final PluginEnableEvent event)
    {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            if (selectionManager.containsKey(player)) {
                return;
            }

            selectionManager.put(player, new SelectionManager());
        }
    }
}
