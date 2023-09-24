package com.zqo.betterworldeditor.handlers;

import com.google.common.reflect.ClassPath;
import com.zqo.betterworldeditor.BetterWorldEditor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public final class ListenerHandler
{
    private final BetterWorldEditor eco = BetterWorldEditor.getBetterWorldEditor();

    public void register()
    {
        final PluginManager pm = eco.getServer().getPluginManager();

        try {
            final ClassPath classPath = ClassPath.from(eco.getClass().getClassLoader());

            classPath.getTopLevelClassesRecursive("com.zqo.betterworldeditor.listeners").forEach((classInfo -> {
                try {
                    final Class<?> c = Class.forName(classInfo.getName());
                    final Object obj = c.getDeclaredConstructor().newInstance();

                    if (obj instanceof Listener listener) {
                        pm.registerEvents(listener, eco);
                    }
                } catch (IllegalAccessException | ClassNotFoundException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
