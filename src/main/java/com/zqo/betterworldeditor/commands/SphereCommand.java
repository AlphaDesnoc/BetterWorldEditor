package com.zqo.betterworldeditor.commands;

import com.zqo.betterworldeditor.BetterWorldEditor;
import com.zqo.betterworldeditor.api.Timer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

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
            return true;
        }

        int radius;
        Material material;

        try {
            radius = Integer.parseInt(args[0]);
            material = Material.matchMaterial(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Le rayon doit être un nombre entier.");
            return true;
        }

        if (material == null) {
            player.sendMessage("Matériau invalide.");
            return true;
        }

        final World world = player.getWorld();
        final Location playerLocation = player.getLocation();
        final Vector direction = playerLocation.getDirection().normalize();

        final Location center = playerLocation.clone().add(direction.multiply(radius + 1));

        timer.start();

        Bukkit.getScheduler().runTask(betterWorldEditor, () -> {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) {
                        final double distanceSquared = x * x + y * y + z * z;

                        if (distanceSquared <= radius * radius) {
                            final Location blockLocation = center.clone().add(x, y, z);

                            world.getBlockAt(blockLocation).setType(Material.AIR);

                            if (distanceSquared >= (radius - 1) * (radius - 1)) {
                                world.getBlockAt(blockLocation).setType(material);
                            }
                        }
                    }
                }
            }

            final double executionTimeSeconds = timer.stop();

            player.sendMessage("Sphère créée avec succès en " + executionTimeSeconds + " secondes.");
        });

        return false;
    }
}
