package com.zqo.betterworldeditor.api;

public final class Timer
{
    private long startTime;

    public void start()
    {
        startTime = System.currentTimeMillis();
    }

    public double stop()
    {
        long endTime = System.currentTimeMillis();
        return (endTime - startTime) / 1000.0;
    }
}
