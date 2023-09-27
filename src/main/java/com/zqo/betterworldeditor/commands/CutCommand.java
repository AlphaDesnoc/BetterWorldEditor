package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class CutCommand extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<UUID, SelectionManager> selectionManagerMap = betterWorldEditor.getSelectionManagerMap();
    private final Timer timer = new Timer();

    public CutCommand()
    {
        super("cut");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        final UUID uuid = player.getUniqueId();
        final SelectionManager selectionManager = selectionManagerMap.get(uuid);

        if (!selectionManager.hasCompleteSelection()) {
            player.sendMessage("Vous devez définir une sélection avec le bâton en fer avant de cut quelque chose.");
            return true;
        }

        final List<BlockData> blockDataList = new ArrayList<>();
        final List<UndoBlocks> undoBlocksList = betterWorldEditor.getUndoBlocksList(uuid);

        timer.start();

        betterWorldEditor.getServer().getScheduler().runTask(betterWorldEditor, () -> {
            final int counter = cutBlocks(selectionManager.getFirstSelection(), selectionManager.getSecondSelection(), blockDataList);

            UndoUtils.addActionToList(undoBlocksList, Actions.CUT, blockDataList);

            final double executionTimeSeconds = timer.stop();

            player.sendMessage("Découpe effectuée de " + counter + " blocs en " + executionTimeSeconds + " secondes.");
        });

        return true;
    }

    private int cutBlocks(final Location firstSelection, final Location secondSelection, final List<BlockData> blockDataList)
    {
        int counter = 0;

        for (int x = Math.min(firstSelection.getBlockX(), secondSelection.getBlockX()); x <= Math.max(firstSelection.getBlockX(), secondSelection.getBlockX()); x++) {
            for (int y = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY()); y <= Math.max(firstSelection.getBlockY(), secondSelection.getBlockY()); y++) {
                for (int z = Math.min(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z <= Math.max(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z++) {
                    final Location location = new Location(firstSelection.getWorld(), x, y, z);
                    final Block block = location.getBlock();

                    if (block.getType() != Material.AIR) {
                        blockDataList.add(new BlockData(block.getLocation(), block.getType(), block.getBlockData()));
                        block.setType(Material.AIR);
                        counter++;
                    }
                }
            }
        }
        return counter;
    }
}
