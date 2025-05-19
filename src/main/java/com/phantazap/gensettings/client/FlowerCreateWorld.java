package com.phantazap.gensettings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.util.Random;


public class FlowerCreateWorld {
    public static void createWorld(Minecraft minecraft) {
        minecraft.setWorld(null);
        System.gc();
        FlowerWorldGenerator worldGenerator = new FlowerWorldGenerator();
        World world = worldGenerator.generateLevel();
        minecraft.setWorld(world);
    }
}
