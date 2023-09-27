package com.zqo.betterworldeditor.api;

import org.bukkit.Location;
import org.bukkit.Material;

public record BlockData(Location location, Material material, org.bukkit.block.data.BlockData blockData) {}
