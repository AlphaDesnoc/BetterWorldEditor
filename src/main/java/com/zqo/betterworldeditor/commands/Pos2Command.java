package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.listeners.SelectionListener;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public final class Pos2Command extends BukkitCommand
{
    public Pos2Command()
    {
        super("pos2");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Cette commande est réservée aux joueurs.");
            return false;
        }

        final Location location = player.getLocation();

        SelectionListener.setSecondSelection(location, player);

        return false;
    }
}
