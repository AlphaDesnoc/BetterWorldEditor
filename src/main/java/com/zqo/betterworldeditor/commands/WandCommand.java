package com.zqo.betterworldeditor.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class WandCommand extends BukkitCommand
{
    public WandCommand()
    {
        super("wand");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        final int itemSlot = player.getInventory().getHeldItemSlot();

        player.getInventory().setItem(itemSlot, new ItemStack(Material.IRON_AXE));

        return false;
    }
}
