package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.listeners.SelectionListener;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public final class SetCommand extends BukkitCommand
{
    public SetCommand()
    {
        super("set");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        final Location firstSelection = SelectionListener.getFirstSelection();
        final Location secondSelection = SelectionListener.getSecondSelection();

        if (firstSelection == null || secondSelection == null) {
            player.sendMessage("Sélections manquantes.");
            return false;
        }

        if (args.length == 1) {
            final String preMat = "minecraft:";
            final String tempMaterial = preMat + args[0];
            final Material material = Material.matchMaterial(tempMaterial);

            if (material == null) {
                player.sendMessage("Material invalide.");
                return false;
            }

            for (int x = Math.min(firstSelection.getBlockX(), secondSelection.getBlockX()); x <= Math.max(firstSelection.getBlockX(), secondSelection.getBlockX()); x++) {
                for (int y = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY()); y <= Math.max(firstSelection.getBlockY(), secondSelection.getBlockY()); y++) {
                    for (int z = Math.min(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z <= Math.max(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z++) {
                        final Location location = new Location(firstSelection.getWorld(), x, y, z);
                        final Block block = location.getBlock();

                        block.setType(material);
                    }
                }
            }
        }

        return false;
    }
}
