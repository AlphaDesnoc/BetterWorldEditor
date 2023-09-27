package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.BlockData;
import com.zqo.betterworldeditor.api.CopiedBlocks;
import com.zqo.betterworldeditor.api.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class PasteCommand extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<UUID, CopiedBlocks> copiedBlocksMap = betterWorldEditor.getCopiedBlocksMap();
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

        final UUID uuid = player.getUniqueId();
        final CopiedBlocks copiedBlocks = copiedBlocksMap.get(uuid);
        final List<BlockData> copiedBlockData = copiedBlocks.getBlocksData();

        if (copiedBlockData.isEmpty()) {
            player.sendMessage("Aucun bloc copié à coller.");
            return false;
        }

        timer.start();

        betterWorldEditor.getServer().getScheduler().runTask(betterWorldEditor, () -> {
            final int counter = pasteBlocks(player, copiedBlockData);
            final double executionTimeSeconds = timer.stop();

            player.sendMessage("Collage effectué de " + counter + " blocs en " + executionTimeSeconds + " secondes.");
        });

        return true;
    }

    private int pasteBlocks(final Player player, final List<BlockData> copiedBlockData)
    {
        int counter = 0;

        for (final BlockData blockData : copiedBlockData) {
            final Location originalLocation = blockData.location();
            final Location newLocation = player.getLocation()
                    .add(originalLocation.getX() - copiedBlockData.get(0).location().getX(),
                            originalLocation.getY() - copiedBlockData.get(0).location().getY(),
                            originalLocation.getZ() - copiedBlockData.get(0).location().getZ());

            player.getWorld().getBlockAt(newLocation).setType(blockData.material());
            player.getWorld().getBlockAt(newLocation).setBlockData(blockData.blockData());
            counter++;
        }

        return counter;
    }
}
