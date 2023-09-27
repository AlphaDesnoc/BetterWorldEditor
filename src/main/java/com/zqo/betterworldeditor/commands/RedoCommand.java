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
        final List<BlockData> previousBlockDataList = new ArrayList<>();

        if (blockDataList == null) {
            return false;
        }

        betterWorldEditor.getServer().getScheduler().runTask(betterWorldEditor, () -> {
            redoBlocks(blockDataList, previousBlockDataList);

            UndoUtils.addActionToList(undoBlocksList, redoBlocks.getAction(), previousBlockDataList);

            redoBlocksList.remove(redoBlocksList.size()-1);

            player.sendMessage("Blocs restaurés avec succès.");
        });

        return true;
    }

    private void redoBlocks(final List<BlockData> blockDataList, final List<BlockData> previousBlockDataList)
    {
        blockDataList.forEach(blockData -> {
            final Location location = blockData.location();
            final Block block = location.getBlock();

            previousBlockDataList.add(new BlockData(location, block.getType(), block.getBlockData()));

            block.setType(blockData.material());
            block.setBlockData(blockData.blockData());
        });
    }
}
