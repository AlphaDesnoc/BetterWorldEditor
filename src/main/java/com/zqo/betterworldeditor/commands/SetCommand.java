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

public final class SetCommand extends BukkitCommand {
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<UUID, SelectionManager> selectionManagerMap = betterWorldEditor.getSelectionManagerMap();
    private final Timer timer = new Timer();

    public SetCommand()
    {
        super("set");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        final UUID uuid = player.getUniqueId();
        final SelectionManager selectionManager = selectionManagerMap.get(uuid);

        if (!selectionManager.hasCompleteSelection()) {
            player.sendMessage("Vous devez définir une sélection avec le bâton en fer avant de set quelque chose.");
            return true;
        }

        if (args.length == 1) {
            final String preMat = "minecraft:";
            final String tempMaterial = preMat + args[0];
            final Material material = Material.matchMaterial(tempMaterial);

            if (material == null) {
                player.sendMessage("Material invalide.");
                return false;
            }

            final List<BlockData> blockDataList = new ArrayList<>();
            final List<UndoBlocks> undoBlocksList = betterWorldEditor.getUndoBlocksList(uuid);

            timer.start();

            betterWorldEditor.getServer().getScheduler().runTask(betterWorldEditor, () -> {
                final int counter = setBlocks(selectionManager.getFirstSelection(), selectionManager.getSecondSelection(), material, blockDataList);

                UndoUtils.addActionToList(undoBlocksList, Actions.SET, blockDataList);

                final double executionTimeSeconds = timer.stop();

                player.sendMessage("Collage effectué de " + counter + " blocs en " + executionTimeSeconds + " secondes.");
            });
        }

        return true;
    }

    private int setBlocks(final Location firstSelection, final Location secondSelection, final Material material, final List<BlockData> blockDataList)
    {
        int counter = 0;

        for (int x = Math.min(firstSelection.getBlockX(), secondSelection.getBlockX()); x <= Math.max(firstSelection.getBlockX(), secondSelection.getBlockX()); x++) {
            for (int y = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY()); y <= Math.max(firstSelection.getBlockY(), secondSelection.getBlockY()); y++) {
                for (int z = Math.min(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z <= Math.max(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z++) {
                    final Location location = new Location(firstSelection.getWorld(), x, y, z);
                    final Block block = location.getBlock();

                    blockDataList.add(new BlockData(block.getLocation(), block.getType(), block.getBlockData()));
                    block.setType(material);
                    counter++;
                }
            }
        }

        return counter;
    }
}