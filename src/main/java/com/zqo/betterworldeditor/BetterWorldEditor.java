package com.zqo.betterworldeditor;

import com.zqo.betterworldeditor.api.CopiedBlocks;
import com.zqo.betterworldeditor.api.SelectionManager;
import com.zqo.betterworldeditor.commands.ReplaceCommand;
import com.zqo.betterworldeditor.handlers.CommandHandler;
import com.zqo.betterworldeditor.handlers.ListenerHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;


public final class BetterWorldEditor extends JavaPlugin {
    private static BetterWorldEditor betterWorldEditor;
    private static Map<Player, SelectionManager> selectionManagerMap;
    private static Map<Player, CopiedBlocks> copiedBlocksMap;

    @Override
    public void onEnable()
    {
        betterWorldEditor = this;
        selectionManagerMap = new HashMap<>();
        copiedBlocksMap = new HashMap<>();

        new ListenerHandler().register();
        new CommandHandler().register();
    }

    public static BetterWorldEditor getBetterWorldEditor() {
        return betterWorldEditor;
    }

    public Map<Player, SelectionManager> getSelectionManagerMap() {
        return selectionManagerMap;
    }

    public Map<Player, CopiedBlocks> getCopiedBlocksMap() {
        return copiedBlocksMap;
    }
}
