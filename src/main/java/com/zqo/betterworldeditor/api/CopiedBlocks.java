package com.zqo.betterworldeditor.api;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public final class CopiedBlocks
{
    private final List<BlockData> blockDataList = new ArrayList<>();

    public void addBlock(final Block block)
    {
        final Location location = block.getLocation();
        blockDataList.add(new BlockData(location, block.getType(), block.getBlockData()));
    }

    public List<BlockData> getBlocksData()
    {
        return blockDataList;
    }

    public void clear()
    {
        blockDataList.clear();
    }
}
