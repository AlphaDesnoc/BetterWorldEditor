package com.zqo.betterworldeditor.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.List;

public final class PasteCommand extends BukkitCommand
{
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

        final List<Block> pasteBlocks = CopyCommand.getCopyBlocks();
        final Location playerLocation = player.getLocation();
        final World world = playerLocation.getWorld();

        for (Block block : pasteBlocks) {
            final Location originalLocation = block.getLocation();
            final Location newLocation = new Location(world, playerLocation.getX() + (originalLocation.getX() - pasteBlocks.get(0).getX()),
                    playerLocation.getY() + (originalLocation.getY() - pasteBlocks.get(0).getY()),
                    playerLocation.getZ() + (originalLocation.getZ() - pasteBlocks.get(0).getZ()));

            world.getBlockAt(newLocation).setType(block.getType());
            world.getBlockAt(newLocation).setBlockData(block.getBlockData());
        }

        player.sendMessage("Collage effectué.");

        return false;
    }
}
