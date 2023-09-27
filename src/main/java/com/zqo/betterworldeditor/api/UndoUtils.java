package com.zqo.betterworldeditor.api;

import java.util.List;

public final class UndoUtils
{
    public static void addActionToList(
            final List<UndoBlocks> undoBlocksList,
            final ActionsEditor action,
            final List<BlockData> blockDataList
    )
    {
        undoBlocksList.add(new UndoBlocks(action));

        final UndoBlocks undoBlocks = undoBlocksList.get(undoBlocksList.size()-1);

        undoBlocks.addUndoBlocks(blockDataList);
    }
}
