package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.SelectionManager;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

import java.util.Map;

public final class Pos2Command extends BukkitCommand
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<Player, SelectionManager> selectionManagerMap = betterWorldEditor.getSelectionManagerMap();

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
        final SelectionManager selectionManager = selectionManagerMap.get(player);

        selectionManager.setSecondSelection(location, player);

        return false;
    }
}
