package com.phantazap.gensettings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

import java.util.Random;


public class CGCreateWorld {
    public static void createWorld(Minecraft minecraft, int length, int width, int height, int type, int theme, Random seed, int surroundingWaterHeight, int surroundingGroundHeight) {
        minecraft.setWorld((World)null);
        System.gc();
        String username = minecraft.session != null ? minecraft.session.username : "anonymous";
        CGWorldGenerator worldGenerator;
        (worldGenerator = new CGWorldGenerator(minecraft.progressRenderer)).island = type == 1;
        worldGenerator.floating = type == 2;
        worldGenerator.flat = type == 3;
        worldGenerator.theme = theme;

        World world = worldGenerator.generate(username, width, length, height, seed, surroundingWaterHeight, surroundingGroundHeight);
        minecraft.setWorld(world);
    }
}
