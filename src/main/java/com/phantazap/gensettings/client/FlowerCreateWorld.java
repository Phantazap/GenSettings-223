package com.phantazap.gensettings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.util.Random;


public class FlowerCreateWorld {
    public static void createWorld(Minecraft minecraft, int length, int width, int height, Random seed) {
        minecraft.setWorld((World)null);
        System.gc();
        FlowerWorldGenerator worldGenerator;
        worldGenerator = new FlowerWorldGenerator(minecraft.progressRenderer);

        World world = worldGenerator.generateLevel(width, length, height, seed);
        minecraft.setWorld(world);
    }
}
