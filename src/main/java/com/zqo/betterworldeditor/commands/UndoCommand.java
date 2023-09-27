package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.BlockData;
import com.zqo.betterworldeditor.api.UndoBlocks;
import com.zqo.betterworldeditor.api.UndoUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class UndoCommand extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<UUID, List<UndoBlocks>> undoBlocksMap = betterWorldEditor.getUndoBlocksMap();
    private final Map<UUID, List<UndoBlocks>> redoBlocksMap = betterWorldEditor.getRedoBlocksMap();

    public UndoCommand()
    {
        super("undo");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        final UUID uuid = player.getUniqueId();

        if (!undoBlocksMap.containsKey(uuid) || undoBlocksMap.get(uuid).isEmpty()) {
            return false;
        }

        if (!redoBlocksMap.containsKey(uuid)) {
            return false;
        }

        final List<UndoBlocks> undoBlocksList = betterWorldEditor.getUndoBlocksList(uuid);
        final List<UndoBlocks> redoBlocksList = betterWorldEditor.getRedoBlocksList(uuid);
        final UndoBlocks undoBlocks = undoBlocksList.get(undoBlocksList.size()-1);
        final List<BlockData> blockDataList = undoBlocks.getUndoBlocks();

        if (blockDataList == null) {
            return false;
        }

        final List<BlockData> previousBlockDataList = new ArrayList<>();

        blockDataList.forEach(blockData -> {
            final Location location = blockData.getLocation();
            final Block block = location.getBlock();

            previousBlockDataList.add(new BlockData(location, block.getType(), block.getBlockData()));

            block.setType(blockData.getMaterial());
            block.setBlockData(blockData.getBlockData());
        });

        UndoUtils.addActionToList(redoBlocksList, undoBlocks.getAction(), previousBlockDataList);
        undoBlocksList.remove(undoBlocksList.size()-1);

        player.sendMessage("[UNDO] Blocs restaurés avec succès.");
        return false;
    }
}
