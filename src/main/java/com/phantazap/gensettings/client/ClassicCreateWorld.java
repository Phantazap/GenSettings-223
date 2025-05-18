package com.phantazap.gensettings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.util.Random;


public class ClassicCreateWorld {
    public static void createWorld(Minecraft minecraft, int length, int width, int height, Random seed) {
        minecraft.setWorld((World)null);
        System.gc();
        String username = minecraft.session != null ? minecraft.session.username : "anonymous";
        ClassicLevelGenerator worldGenerator;
        worldGenerator = new ClassicLevelGenerator(minecraft.progressRenderer);

        World world = worldGenerator.generateLevel(username, width, length, height, seed);
        minecraft.setWorld(world);
    }
}
