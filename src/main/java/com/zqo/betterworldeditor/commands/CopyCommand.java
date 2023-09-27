package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.CopiedBlocks;
import com.zqo.betterworldeditor.api.SelectionManager;
import com.zqo.betterworldeditor.api.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;

public final class CopyCommand extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<UUID, SelectionManager> selectionManagerMap = betterWorldEditor.getSelectionManagerMap();
    private final Map<UUID, CopiedBlocks> copiedBlocksMap = betterWorldEditor.getCopiedBlocksMap();
    private final Timer timer = new Timer();

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

        final UUID uuid = player.getUniqueId();
        final SelectionManager selectionManager = selectionManagerMap.get(uuid);
        final CopiedBlocks copiedBlocks = copiedBlocksMap.get(uuid);

        if (!selectionManager.hasCompleteSelection()) {
            player.sendMessage("Vous devez définir une sélection avec le bâton en fer avant de copy quelque chose.");
            return false;
        }

        if (!copiedBlocks.getBlocksData().isEmpty()) {
            copiedBlocks.clear();
        }

        timer.start();

        betterWorldEditor.getServer().getScheduler().runTaskAsynchronously(betterWorldEditor, () -> {
            final int counter = copyBlocks(selectionManager.getFirstSelection(), selectionManager.getSecondSelection(), copiedBlocks);
            final double executionTimeSeconds = timer.stop();

            player.sendMessage("Copie effectuée de " + counter + " blocs en " + executionTimeSeconds + " secondes.");
        });

        return true;
    }

    private int copyBlocks(final Location firstSelection, final Location secondSelection, final CopiedBlocks copiedBlocks)
    {
        int counter = 0;

        final org.bukkit.World world = firstSelection.getWorld();
        final int minX = Math.min(firstSelection.getBlockX(), secondSelection.getBlockX());
        final int minY = Math.min(firstSelection.getBlockY(), secondSelection.getBlockY());
        final int minZ = Math.min(firstSelection.getBlockZ(), secondSelection.getBlockZ());
        final int maxX = Math.max(firstSelection.getBlockX(), secondSelection.getBlockX());
        final int maxY = Math.max(firstSelection.getBlockY(), secondSelection.getBlockY());
        final int maxZ = Math.max(firstSelection.getBlockZ(), secondSelection.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    final Location location = new Location(world, x, y, z);
                    final org.bukkit.block.Block block = location.getBlock();

                    copiedBlocks.addBlock(block);
                    counter++;
                }
            }
        }

        return counter;
    }
}
