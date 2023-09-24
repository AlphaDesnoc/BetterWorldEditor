package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.listeners.SelectionListener;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public final class Pos1Command extends BukkitCommand
{
    public Pos1Command()
    {
        super("pos1");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        final Location location = player.getLocation();

        SelectionListener.setFirstSelection(location, player);

        return false;
    }
}
