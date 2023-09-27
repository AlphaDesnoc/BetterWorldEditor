package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public final class PyramidCommand extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Timer timer = new Timer();

    public PyramidCommand()
    {
        super("pyramid");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        if (args.length < 2) {
            player.sendMessage("Utilisation : /pyramid <hauteur> <matériau>");
            return true;
        }

        int height;
        Material material;

        try {
            height = Integer.parseInt(args[0]);
            material = Material.matchMaterial(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("La hauteur doit être un nombre entier.");
            return true;
        }

        if (material == null) {
            player.sendMessage("Matériau invalide.");
            return true;
        }

        final World world = player.getWorld();
        final Location playerLocation = player.getLocation();
        final Vector direction = playerLocation.getDirection().normalize();

        final Location baseCenter = playerLocation.clone().add(direction.multiply(height / 2));
        final int pyramidWidth = height * 2 - 1;

        timer.start();

        final List<BlockData> blockDataList = new ArrayList<>();

        Bukkit.getScheduler().runTask(betterWorldEditor, () -> {
            for (int y = 0; y < height; y++) {
                int blocksInLevel = pyramidWidth - y * 2;

                for (int x = -blocksInLevel / 2; x <= blocksInLevel / 2; x++) {
                    for (int z = -blocksInLevel / 2; z <= blocksInLevel / 2; z++) {
                        final Location blockLocation = baseCenter.clone().add(x, y, z);

                        if (y == 0 || Math.abs(x) == blocksInLevel / 2 || Math.abs(z) == blocksInLevel / 2) {
                            final Block block = world.getBlockAt(blockLocation);
                            blockDataList.add(new BlockData(block.getLocation(), block.getType(), block.getBlockData()));
                            block.setType(material);
                        }
                    }
                }
            }

            final List<UndoBlocks> undoBlocksList = betterWorldEditor.getUndoBlocksList(player.getUniqueId());

            UndoUtils.addActionToList(undoBlocksList, ActionsEditor.PYRAMID, blockDataList);

            final double executionTimeSeconds = timer.stop();

            player.sendMessage("Pyramide créée avec succès en " + executionTimeSeconds + " secondes.");
        });

        return false;
    }
}