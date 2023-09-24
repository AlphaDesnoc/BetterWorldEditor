package com.zqo.betterworldeditor;

import com.zqo.betterworldeditor.api.CopiedBlocks;
import com.zqo.betterworldeditor.api.SelectionManager;
import com.zqo.betterworldeditor.commands.ReplaceCommand;
import com.zqo.betterworldeditor.handlers.CommandHandler;
import com.zqo.betterworldeditor.handlers.ListenerHandler;
import org.bukkit.plugin.java.JavaPlugin;


public final class BetterWorldEditor extends JavaPlugin {
    private static BetterWorldEditor betterWorldEditor;
    private SelectionManager selectionManager;
    private CopiedBlocks copiedBlocks;

    @Override
    public void onEnable()
    {
        betterWorldEditor = this;
        selectionManager = new SelectionManager();
        copiedBlocks = new CopiedBlocks();

        new ListenerHandler().register();
        new CommandHandler().register();
    }

    public static BetterWorldEditor getBetterWorldEditor() {
        return betterWorldEditor;
    }

    public SelectionManager getSelectionManager()
    {
        return selectionManager;
    }

    public CopiedBlocks getCopiedBlocks()
    {
        return copiedBlocks;
    }
}
