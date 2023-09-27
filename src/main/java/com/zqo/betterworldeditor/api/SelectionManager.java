package com.zqo.betterworldeditor.api;

import com.zqo.betterworldeditor.BetterWorldEditor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public final class SelectionManager {
    private final BetterWorldEditor betterWorldEditor = BetterWorldEditor.getBetterWorldEditor();
    private Location firstSelection;
    private Location secondSelection;
    private BukkitTask particleTask = null;

    public void setFirstSelection(final Location location, final Player player) {
        sendMessageAndSetSelection(location, player, "Première sélection");
    }

    public void setSecondSelection(final Location location, final Player player) {
        sendMessageAndSetSelection(location, player, "Deuxième sélection");
    }

    private void sendMessageAndSetSelection(final Location location, final Player player, final String message) {
        player.sendMessage(message + " : X" + Math.round(location.getX()) + " Y" + Math.round(location.getY()) + " Z" + Math.round(location.getZ()));
        if (message.startsWith("Deuxième")) {
            this.secondSelection = location;
        } else {
            this.firstSelection = location;
        }
        startParticleTask(player);
    }

    public Location getFirstSelection() {
        return firstSelection;
    }

    public Location getSecondSelection() {
        return secondSelection;
    }

    public boolean hasCompleteSelection() {
        return firstSelection != null && secondSelection != null;
    }

    private void startParticleTask(final Player player) {
        if (particleTask != null) {
            particleTask.cancel();
        }

        if (hasCompleteSelection()) {
            final BukkitRunnable particleRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    showSelectionParticles();
                }
            };

            particleTask = particleRunnable.runTaskTimer(betterWorldEditor, 0L, 20L);
        }
    }

    private void showSelectionParticles() {
        if (hasCompleteSelection()) {
            final Location loc1 = getFirstSelection();
            final Location loc2 = getSecondSelection();

            final double minX = Math.min(loc1.getX(), loc2.getX());
            final double minY = Math.min(loc1.getY(), loc2.getY());
            final double minZ = Math.min(loc1.getZ(), loc2.getZ());

            final double maxX = Math.max(loc1.getX(), loc2.getX()) + 1;
            final double maxY = Math.max(loc1.getY(), loc2.getY()) + 1;
            final double maxZ = Math.max(loc1.getZ(), loc2.getZ()) + 1;

            final double particleDensity = 0.1;
            final Color color = generateColor();

            for (double y = minY; y <= maxY; y += particleDensity) {
                for (double z = minZ; z <= maxZ; z += particleDensity) {
                    if (isEdge(minY, maxY, minZ, maxZ, y, z, particleDensity)) {
                        spawnParticle(minX, y, z, color);
                        spawnParticle(maxX, y, z, color);
                    }
                }
            }

            for (double x = minX; x <= maxX; x += particleDensity) {
                for (double z = minZ; z <= maxZ; z += particleDensity) {
                    if (isEdge(minX, maxX, minZ, maxZ, x, z, particleDensity)) {
                        spawnParticle(x, minY, z, color);
                        spawnParticle(x, maxY, z, color);
                    }
                }
            }
        }
    }

    private boolean isEdge(double minCoord1, double maxCoord1, double minCoord2, double maxCoord2, double coord1, double coord2, double density) {
        return coord1 < minCoord1 + density || coord1 > maxCoord1 - density ||
                coord2 < minCoord2 + density || coord2 > maxCoord2 - density;
    }

    private void spawnParticle(final double x, final double y, final double z, final Color color) {
        for (final Player plr : Bukkit.getOnlinePlayers()) {
            plr.spawnParticle(
                    Particle.REDSTONE,
                    x,
                    y,
                    z,
                    0,
                    0,
                    0,
                    1,
                    new Particle.DustOptions(color, 1)
            );
        }
    }

    private Color generateColor() {
        final Random random = new Random();
        return Color.fromRGB(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
}