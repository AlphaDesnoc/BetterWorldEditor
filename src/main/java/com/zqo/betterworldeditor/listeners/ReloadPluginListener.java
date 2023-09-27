package com.zqo.betterworldeditor.listeners;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.SelectionManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public final class ReloadPluginListener implements Listener {
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<UUID, SelectionManager> selectionManager = betterWorldEditor.getSelectionManagerMap();

    @EventHandler
    public void onJoin(final PluginEnableEvent event)
    {
        for (final Player player : Bukkit.getOnlinePlayers()) {
            final UUID uuid = player.getUniqueId();

            if (!selectionManager.containsKey(uuid)) {
                selectionManager.put(uuid, new SelectionManager());
            }

            if (!betterWorldEditor.getUndoBlocksMap().containsKey(uuid)) {
                betterWorldEditor.getUndoBlocksMap().put(uuid, new ArrayList<>());
            }

            if (!betterWorldEditor.getRedoBlocksMap().containsKey(uuid)) {
                betterWorldEditor.getRedoBlocksMap().put(uuid, new ArrayList<>());
            }
        }
    }
}
