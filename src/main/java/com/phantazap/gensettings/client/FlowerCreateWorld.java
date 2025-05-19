package com.phantazap.gensettings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.util.Random;


public class FlowerCreateWorld {
    public static void createWorld(Minecraft minecraft, int length, int width, int height) {
        minecraft.setWorld(null);
        System.gc();
        FlowerWorldGenerator worldGenerator = new FlowerWorldGenerator(minecraft.progressRenderer);
        World world = worldGenerator.generateLevel(width, length, height);
        minecraft.setWorld(world);
    }
}
