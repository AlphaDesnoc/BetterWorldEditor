package com.zqo.betterworldeditor.listeners;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.SelectionManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public final class SelectionListener implements Listener
{
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private final Map<Player, SelectionManager> selectionManagerMap = betterWorldEditor.getSelectionManagerMap();

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

        final SelectionManager selectionManager = selectionManagerMap.get(player);

        event.setCancelled(true);

        switch (action) {
            case LEFT_CLICK_BLOCK -> {
                selectionManager.setFirstSelection(block.getLocation(), player);
            }
            case RIGHT_CLICK_BLOCK -> {
                selectionManager.setSecondSelection(block.getLocation(), player);
            }
        }
    }
}
