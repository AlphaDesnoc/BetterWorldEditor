package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.SelectionManager;
import com.zqo.betterworldeditor.api.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Map;

public final class SetCommand extends BukkitCommand {
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<Player, SelectionManager> selectionManagerMap = betterWorldEditor.getSelectionManagerMap();
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

        final SelectionManager selectionManager = selectionManagerMap.get(player);

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

            timer.start();

            Bukkit.getScheduler().runTask(betterWorldEditor, () -> {
                int counter = setBlocks(selectionManager.getFirstSelection(), selectionManager.getSecondSelection(), material);

                final double executionTimeSeconds = timer.stop();

                player.sendMessage("Collage effectué de " + counter + " blocs en " + executionTimeSeconds + " secondes.");
            });
        }
        return false;
    }

    private int setBlocks(final Location firstSelection, final Location secondSelection, final Material material)
    {
        int counter = 0;

        for (int x = Math.min(firstSelection.getBlockX(), secondSelection.getBlockX()); x <= Math.max(firstSelection.getBlockX(), secondSelection.getBlockX()); x++) {
            for (int y = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY()); y <= Math.max(firstSelection.getBlockY(), secondSelection.getBlockY()); y++) {
                for (int z = Math.min(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z <= Math.max(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z++) {
                    final Location location = new Location(firstSelection.getWorld(), x, y, z);
                    final Block block = location.getBlock();

                    block.setType(material);
                    counter++;
                }
            }
        }

        return counter;
    }
}