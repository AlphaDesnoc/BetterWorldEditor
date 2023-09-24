package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.listeners.SelectionListener;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class CopyCommand extends BukkitCommand
{
    private static final List<Block> copyBlocks = new ArrayList<>();

    public CopyCommand()
    {
        super("copy");
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

        for (int x = Math.min(firstSelection.getBlockX(), secondSelection.getBlockX()); x <= Math.max(firstSelection.getBlockX(), secondSelection.getBlockX()); x++) {
            for (int y = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY()); y <= Math.max(firstSelection.getBlockY(), secondSelection.getBlockY()); y++) {
                for (int z = Math.min(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z <= Math.max(firstSelection.getBlockZ(), secondSelection.getBlockZ()); z++) {
                    final Location location = new Location(firstSelection.getWorld(), x, y, z);
                    final org.bukkit.block.Block block = location.getBlock();

                    copyBlocks.add(block);
                }
            }
        }

        player.sendMessage("Copie effectuée.");
        return false;
    }

    public static List<Block> getCopyBlocks() {
        return copyBlocks;
    }
}
