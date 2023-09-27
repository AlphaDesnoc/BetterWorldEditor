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

public final class SphereCommand extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Timer timer = new Timer();

    public SphereCommand()
    {
        super("sphere");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        if (args.length < 2) {
            player.sendMessage("Utilisation : /sphere <rayon> <matériau>");
            return false;
        }

        int radius;
        Material material;

        try {
            radius = Integer.parseInt(args[0]);
            material = Material.matchMaterial(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Le rayon doit être un nombre entier.");
            return false;
        }

        if (material == null) {
            player.sendMessage("Matériau invalide.");
            return false;
        }

        final World world = player.getWorld();
        final Location playerLocation = player.getLocation();
        final Vector direction = playerLocation.getDirection().normalize();

        final Location center = playerLocation.clone().add(direction.multiply(radius + 1));
        final List<UndoBlocks> undoBlocksList = betterWorldEditor.getUndoBlocksList(player.getUniqueId());
        final List<BlockData> blockDataList = new ArrayList<>();

        timer.start();

        betterWorldEditor.getServer().getScheduler().runTask(betterWorldEditor, () -> {
            createSphere(radius, world, center, material, blockDataList);

            UndoUtils.addActionToList(undoBlocksList, Actions.SPHERE, blockDataList);

            final double executionTimeSeconds = timer.stop();

            player.sendMessage("Sphère créée avec succès en " + executionTimeSeconds + " secondes.");
        });

        return true;
    }

    private void createSphere(final int radius, final World world, final Location center, final Material material, final List<BlockData> blockDataList)
    {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    final double distanceSquared = x * x + y * y + z * z;

                    if (distanceSquared <= radius * radius) {
                        final Location blockLocation = center.clone().add(x, y, z);
                        final Block block = world.getBlockAt(blockLocation);

                        if (!block.getType().equals(Material.AIR)) {
                            blockDataList.add(new BlockData(block.getLocation(), block.getType(), block.getBlockData()));
                        }

                        block.setType(Material.AIR);

                        if (distanceSquared >= (radius - 1) * (radius - 1)) {
                            block.setType(material);
                        }
                    }
                }
            }
        }
    }
}
