package com.zqo.betterworldeditor.api;

import java.util.ArrayList;
import java.util.List;

public final class UndoBlocks
{
    private List<List<BlockData>> blockDataList;
    private ActionsEditor action;

    public UndoBlocks(final ActionsEditor action)
    {
        this.action = action;
        this.blockDataList = new ArrayList<>();
    }

    public List<BlockData> getUndoBlocks()
    {
        return !this.blockDataList.isEmpty() ? this.blockDataList.get(0) : null;
    }

    public void addUndoBlocks(List<BlockData> blockDataList)
    {
        this.blockDataList.add(blockDataList);
    }

    public void clearBlockDataList()
    {
        this.blockDataList.clear();
    }

    public ActionsEditor getAction()
    {
        return this.action;
    }

    public void setAction(ActionsEditor action)
    {
        this.action = action;
    }
}
