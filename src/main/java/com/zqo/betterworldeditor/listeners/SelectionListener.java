package com.zqo.betterworldeditor.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class SelectionListener implements Listener
{
    private static Location firstSelection;
    private static Location secondSelection;
    private static Location corner1;
    private static Location corner2;

    @EventHandler
    public void onSelect(final PlayerInteractEvent event)
    {
        final ItemStack item = event.getItem();
        final Action action = event.getAction();
        final Block block = event.getClickedBlock();
        final Player player = event.getPlayer();

        if (item == null) {
            return;
        }

        if (!item.getType().equals(Material.IRON_AXE)) {
            return;
        }

        event.setCancelled(true);

        switch (action) {
            case LEFT_CLICK_BLOCK -> {
                setFirstSelection(block.getLocation(), player);
            }
            case RIGHT_CLICK_BLOCK -> {
                setSecondSelection(block.getLocation(), player);
            }
        }
    }

    public static Location getFirstSelection() {
        return firstSelection;
    }

    public static Location getSecondSelection() {
        return secondSelection;
    }

    public static void setFirstSelection(final Location firstSelection, final Player player) {
        player.sendMessage("Première séléction: X" + Math.round(firstSelection.getX()) + " Y" + Math.round(firstSelection.getY()) + " Z" + Math.round(firstSelection.getZ()));
        SelectionListener.firstSelection = firstSelection;
    }

    public static void setSecondSelection(final Location secondSelection, final Player player) {
        player.sendMessage("Seconde séléction: X" + Math.round(secondSelection.getX()) + " Y" + Math.round(secondSelection.getY()) + " Z" + Math.round(secondSelection.getZ()));
        SelectionListener.secondSelection = secondSelection;
    }
}
