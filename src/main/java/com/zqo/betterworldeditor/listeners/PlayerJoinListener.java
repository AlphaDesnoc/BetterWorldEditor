package com.zqo.betterworldeditor.listeners;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.CopiedBlocks;
import com.zqo.betterworldeditor.api.SelectionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.UUID;

public final class PlayerJoinListener implements Listener {
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();

    @EventHandler
    public void onJoin(final PlayerJoinEvent event)
    {
        final UUID uuid = event.getPlayer().getUniqueId();

        if (!betterWorldEditor.getSelectionManagerMap().containsKey(uuid)) {
            betterWorldEditor.getSelectionManagerMap().put(uuid, new SelectionManager());
        }

        if (!betterWorldEditor.getUndoBlocksMap().containsKey(uuid)) {
            betterWorldEditor.getUndoBlocksMap().put(uuid, new ArrayList<>());
        }

        if (!betterWorldEditor.getRedoBlocksMap().containsKey(uuid)) {
            betterWorldEditor.getRedoBlocksMap().put(uuid, new ArrayList<>());
        }

        if (!betterWorldEditor.getCopiedBlocksMap().containsKey(uuid)) {
            betterWorldEditor.getCopiedBlocksMap().put(uuid, new CopiedBlocks());
        }
    }
}
