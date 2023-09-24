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

public final class ReplaceCommand extends BukkitCommand
{
    private long counter;
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final SelectionManager selectionManager = betterWorldEditor.getSelectionManager();
    private final Timer timer = new Timer();

    public ReplaceCommand()
    {
        super("replace");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        if (!selectionManager.hasCompleteSelection()) {
            player.sendMessage("Vous devez définir une sélection avec le bâton en fer avant de replace quelque chose.");
            return true;
        }

        if (args.length == 2) {
            final String preMat = "minecraft:";
            final String tempMaterialToBeReplace = preMat + args[0];
            final String tempMaterialToReplace = preMat + args[1];
            final Material materialToBeReplace = Material.matchMaterial(tempMaterialToBeReplace);
            final Material materialToReplace = Material.matchMaterial(tempMaterialToReplace);

            if (materialToBeReplace == null || materialToReplace == null) {
                player.sendMessage("Material invalide.");
                return false;
            }

            timer.start();

            Bukkit.getScheduler().runTask(betterWorldEditor, () -> {
                replaceBlocks(selectionManager.getFirstSelection(), selectionManager.getSecondSelection(), materialToBeReplace, materialToReplace);

                final double executionTimeSeconds = timer.stop();

                player.sendMessage("Replacement effectué de " + counter + " blocs en " + executionTimeSeconds + " secondes.");
            });
        }

        return false;
    }

    private void replaceBlocks(final Location firstSelection, final Location secondSelection, final Material materialToBeReplace, final Material materialToReplace)
    {
        for (int x = Math.min(firstSelection.getBlockX(), secondSelection.getBlockX()); x <= Math.max(firstSelection.getBlockX(), secondSelection.getBlockX()); x++) {
            for (int y = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY()); y <= Math.max(firstSelection.getBlockY(), secondSelection.getBlockY()); y++) {
                for (int z = Math.min(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z <= Math.max(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z++) {
                    final Location location = new Location(firstSelection.getWorld(), x, y, z);
                    final Block block = location.getBlock();

                    if (block.getType().equals(materialToBeReplace)) {
                        block.setType(materialToReplace);
                        counter++;
                    }
                }
            }
        }
    }
}
