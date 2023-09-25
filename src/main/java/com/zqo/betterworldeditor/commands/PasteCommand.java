package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.BlockData;
import com.zqo.betterworldeditor.api.CopiedBlocks;
import com.zqo.betterworldeditor.api.SelectionManager;
import com.zqo.betterworldeditor.api.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public final class PasteCommand extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<Player, CopiedBlocks> copiedBlocksMap = betterWorldEditor.getCopiedBlocksMap();
    private final Timer timer = new Timer();

    public PasteCommand()
    {
        super("paste");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        final CopiedBlocks copiedBlocks = copiedBlocksMap.get(player);
        final List<BlockData> copiedBlockData = copiedBlocks.getBlocksData();

        if (copiedBlockData.isEmpty()) {
            player.sendMessage("Aucun bloc copié à coller.");
            return true;
        }

        timer.start();

        Bukkit.getScheduler().runTask(betterWorldEditor, () -> {
            int counter = 0;

            for (final BlockData blockData : copiedBlockData) {
                final Location originalLocation = blockData.getLocation();
                final Location newLocation = player.getLocation()
                        .add(originalLocation.getX() - copiedBlockData.get(0).getLocation().getX(),
                                originalLocation.getY() - copiedBlockData.get(0).getLocation().getY(),
                                originalLocation.getZ() - copiedBlockData.get(0).getLocation().getZ());

                player.getWorld().getBlockAt(newLocation).setType(blockData.getMaterial());
                player.getWorld().getBlockAt(newLocation).setBlockData(blockData.getBlockData());
                counter++;
            }

            final double executionTimeSeconds = timer.stop();

            player.sendMessage("Collage effectué de " + counter + " blocs en " + executionTimeSeconds + " secondes.");
        });

        return false;
    }
}
