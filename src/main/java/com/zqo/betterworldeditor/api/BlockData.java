package com.zqo.betterworldeditor.api;

import org.bukkit.Location;
import org.bukkit.Material;

public final class BlockData
{
    private final Location location;
    private final Material material;
    private final org.bukkit.block.data.BlockData blockData;

    public BlockData(Location location, Material material, org.bukkit.block.data.BlockData blockData)
    {
        this.location = location;
        this.material = material;
        this.blockData = blockData;
    }

    public Location getLocation()
    {
        return location;
    }

    public Material getMaterial()
    {
        return material;
    }

    public org.bukkit.block.data.BlockData getBlockData()
    {
        return blockData;
    }
}
