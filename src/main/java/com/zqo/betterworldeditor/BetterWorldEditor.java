package com.zqo.betterworldeditor;

import com.zqo.betterworldeditor.api.CopiedBlocks;
import com.zqo.betterworldeditor.api.SelectionManager;
import com.zqo.betterworldeditor.api.UndoBlocks;
import com.zqo.betterworldeditor.commands.ReplaceCommand;
import com.zqo.betterworldeditor.handlers.CommandHandler;
import com.zqo.betterworldeditor.handlers.ListenerHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;


public final class BetterWorldEditor extends JavaPlugin {
    private static BetterWorldEditor betterWorldEditor;
    private static Map<UUID, SelectionManager> selectionManagerMap;
    private static Map<UUID, CopiedBlocks> copiedBlocksMap;
    private static Map<UUID, List<UndoBlocks>> undoBlocksMap;
    private static Map<UUID, List<UndoBlocks>> redoBlocksMap;

    @Override
    public void onEnable()
    {
        betterWorldEditor = this;
        selectionManagerMap = new HashMap<>();
        copiedBlocksMap = new HashMap<>();
        undoBlocksMap = new HashMap<>();
        redoBlocksMap = new HashMap<>();

        new ListenerHandler().register();
        new CommandHandler().register();
    }

    public static BetterWorldEditor getBetterWorldEditor()
    {
        return betterWorldEditor;
    }

    public Map<UUID, SelectionManager> getSelectionManagerMap()
    {
        return selectionManagerMap;
    }

    public Map<UUID, CopiedBlocks> getCopiedBlocksMap()
    {
        return copiedBlocksMap;
    }

    public Map<UUID, List<UndoBlocks>> getUndoBlocksMap()
    {
        return undoBlocksMap;
    }

    public List<UndoBlocks> getUndoBlocksList(final UUID uuid)
    {
        return this.getUndoBlocksMap().get(uuid);
    }

    public Map<UUID, List<UndoBlocks>> getRedoBlocksMap() {
        return redoBlocksMap;
    }

    public List<UndoBlocks> getRedoBlocksList(final UUID uuid)
    {
        return this.getRedoBlocksMap().get(uuid);
    }
}
