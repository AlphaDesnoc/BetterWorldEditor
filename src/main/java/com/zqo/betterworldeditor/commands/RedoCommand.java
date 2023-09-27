package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.BlockData;
import com.zqo.betterworldeditor.api.UndoBlocks;
import com.zqo.betterworldeditor.api.UndoUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class RedoCommand extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<UUID, List<UndoBlocks>> undoBlocksMap = betterWorldEditor.getUndoBlocksMap();
    private final Map<UUID, List<UndoBlocks>> redoBlocksMap = betterWorldEditor.getRedoBlocksMap();

    public RedoCommand()
    {
        super("redo");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        final UUID uuid = player.getUniqueId();

        if (!undoBlocksMap.containsKey(uuid)) {
            return false;
        }

        if (!redoBlocksMap.containsKey(uuid) || redoBlocksMap.get(uuid).isEmpty()) {
            return false;
        }

        final List<UndoBlocks> undoBlocksList = betterWorldEditor.getUndoBlocksList(uuid);
        final List<UndoBlocks> redoBlocksList = betterWorldEditor.getRedoBlocksList(uuid);
        final UndoBlocks redoBlocks = redoBlocksList.get(redoBlocksList.size()-1);
        final List<BlockData> blockDataList = redoBlocks.getUndoBlocks();

        if (blockDataList == null) {
            return false;
        }

        UndoUtils.addActionToList(undoBlocksList, redoBlocks.getAction(), blockDataList);

        blockDataList.forEach(blockData -> {
            final Location location = blockData.getLocation();
            location.getBlock().setType(blockData.getMaterial());
            location.getBlock().setBlockData(blockData.getBlockData());
        });

        redoBlocksList.remove(redoBlocksList.size()-1);

        player.sendMessage("[REDO] Blocs restaurés avec succès.");
        return false;
    }
}
