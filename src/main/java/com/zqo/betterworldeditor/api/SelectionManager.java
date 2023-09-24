package com.zqo.betterworldeditor.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class SelectionManager
{
    private Location firstSelection;
    private Location secondSelection;

    public void setFirstSelection(Location location, Player player)
    {
        player.sendMessage("Première sélection : X" + Math.round(location.getX()) + " Y" + Math.round(location.getY()) + " Z" + Math.round(location.getZ()));
        this.firstSelection = location;
    }

    public void setSecondSelection(Location location, Player player)
    {
        player.sendMessage("Deuxième sélection : X" + Math.round(location.getX()) + " Y" + Math.round(location.getY()) + " Z" + Math.round(location.getZ()));
        this.secondSelection = location;
    }

    public Location getFirstSelection()
    {
        return firstSelection;
    }

    public Location getSecondSelection()
    {
        return secondSelection;
    }

    public boolean hasCompleteSelection()
    {
        return firstSelection != null && secondSelection != null;
    }
}
