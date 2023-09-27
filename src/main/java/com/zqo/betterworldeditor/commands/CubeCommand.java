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
import java.util.UUID;

public final class CubeCommand extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Timer timer = new Timer();
    public CubeCommand()
    {
        super("cube");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        if (args.length < 2) {
            player.sendMessage("Utilisation : /cube <côté> <matériau>");
            return true;
        }

        int sideLength;
        Material material;

        try {
            sideLength = Integer.parseInt(args[0]);
            material = Material.matchMaterial(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("La longueur du côté doit être un nombre entier.");
            return true;
        }

        if (material == null) {
            player.sendMessage("Matériau invalide.");
            return true;
        }

        final UUID uuid = player.getUniqueId();
        final World world = player.getWorld();
        final Location playerLocation = player.getLocation();
        final Vector direction = playerLocation.getDirection().normalize();

        final Location startLocation = playerLocation.clone().add(direction.multiply(sideLength / 2 + 1));
        final List<BlockData> blockDataList = new ArrayList<>();
        final List<UndoBlocks> undoBlocksList = betterWorldEditor.getUndoBlocksList(uuid);

        timer.start();


        betterWorldEditor.getServer().getScheduler().runTask(betterWorldEditor, () -> {
            createCube(world, sideLength, material, startLocation, blockDataList);

            UndoUtils.addActionToList(undoBlocksList, Actions.CUBE, blockDataList);

            final double executionTimeSeconds = timer.stop();

            player.sendMessage("Cube crée avec succès en " + executionTimeSeconds + " secondes.");
        });

        return false;
    }

    private void createCube(final World world, final int sideLength, final Material material, final Location startLocation, final List<BlockData> blockDataList)
    {
        for (int x = 0; x < sideLength; x++) {
            for (int y = 0; y < sideLength; y++) {
                for (int z = 0; z < sideLength; z++) {
                    if (x == 0 || x == sideLength - 1 ||
                            y == 0 || y == sideLength - 1 ||
                            z == 0 || z == sideLength - 1) {
                        final Location blockLocation = startLocation.clone().add(x, y, z);
                        final Block block = world.getBlockAt(blockLocation);

                        if (!block.getType().equals(Material.AIR)) {
                            blockDataList.add(new BlockData(block.getLocation(), block.getType(), block.getBlockData()));
                        }

                        block.setType(material);
                    }
                }
            }
        }
    }
}
