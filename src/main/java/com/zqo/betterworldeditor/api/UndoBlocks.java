package com.zqo.betterworldeditor.api;

import java.util.ArrayList;
import java.util.List;

public final class UndoBlocks
{
    private final List<List<BlockData>> blockDataList;
    private final Actions action;

    public UndoBlocks(final Actions action)
    {
        this.action = action;
        this.blockDataList = new ArrayList<>();
    }

    public List<BlockData> getUndoBlocks()
    {
        return !this.blockDataList.isEmpty() ? this.blockDataList.get(0) : null;
    }

    public void addUndoBlocks(final List<BlockData> blockDataList)
    {
        this.blockDataList.add(blockDataList);
    }

    public Actions getAction()
    {
        return this.action;
    }
}
