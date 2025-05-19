package com.phantazap.gensettings.client;

import net.minecraft.client.ProgressListener;
import net.minecraft.world.World;
import net.minecraft.world.gen.noise.PerlinNoise;
import net.minecraft.block.Block;

import java.util.Random;

public class FlowerWorldGenerator {
    private final ProgressListener levelLoaderListener;
    private int width;
    private int height;
    public Random seed;
    private byte[] blocks;

    public FlowerWorldGenerator(ProgressListener levelLoaderListener) {
        this.levelLoaderListener = levelLoaderListener;
    }

    public final World generateLevel(int width, int length, int height, Random seed) {
        //Do some initializing stuff
        long start = System.currentTimeMillis();
        this.levelLoaderListener.progressStart("Generating level");
        this.width = width;
        this.height = length;
        this.seed = seed;
        int waterLevel = 1;
        this.blocks = new byte[width * length << 6];
        this.levelLoaderListener.progressStage("Raising..");
        FlowerWorldGenerator levelGen6 = this;
        int[] plane = new int[256 * 256];

        //Necessary to avoid crash
        for(int widthLoop = 0; widthLoop < 256; ++widthLoop) {
            for(int lengthLoop = 0; lengthLoop < 256; ++lengthLoop) {
                int area = plane[widthLoop + lengthLoop * 256] + waterLevel;
                plane[widthLoop + lengthLoop * 256] = area;
            }
        }

        //Advances the seed as many times as it would in the full generator to keep our flowers consistent.
        new PerlinNoise(seed, 78);

        for(int i = 0; i < 512; ++i) {
            seed.nextFloat();
            seed.nextFloat();
            seed.nextFloat();
            int localFloat = (int)((levelGen6.seed.nextFloat() + levelGen6.seed.nextFloat()) * 200.0F);
            seed.nextFloat();
            seed.nextFloat();
            seed.nextFloat();
            seed.nextFloat();

            for(int j = 0; j < localFloat; ++j) {
                seed.nextFloat();
                seed.nextFloat();
                seed.nextFloat();
                seed.nextFloat();
                if(levelGen6.seed.nextFloat() >= 0.25F) {
                    seed.nextFloat();
                    seed.nextFloat();
                    seed.nextFloat();
                }
            }
        }

        this.randomIncOre(90);
        this.randomIncOre(70);
        this.randomIncOre(50);

        for(int i = 0; i < 8; ++i) {
            seed.nextInt(256);
            seed.nextInt(2);
            seed.nextInt(256);
        }

        for(int i = 0; i < 209; ++i) {
            seed.nextInt(256);
            seed.nextInt(256);
            seed.nextFloat();
            seed.nextFloat();
        }

        new PerlinNoise(seed, 16);

        //Grow our flowers
        this.growFlowers(plane);

        //Save settings
        World level34;
        (level34 = new World()).surroundingWaterHeight = waterLevel;
        level34.surroundingGroundHeight = waterLevel;
        level34.assemble(width, 64, length, this.blocks, null);
        level34.timeOfCreation = System.currentTimeMillis();
        level34.author = "Phantazap";
        level34.name = "A Nice World";
        level34.spawnY = 2;
        level34.cloudHeight = height + 2;
        this.levelLoaderListener.progressStage("Spawning..");
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("World generated in " + timeElapsed + " milliseconds.");
        return level34;
    }

    private void growFlowers(int[] blocks) {

        for(int i = 0; i < 21; ++i) {
            int rose = seed.nextInt(2);
            int widthSeed = seed.nextInt(this.width);
            int heightSeed = seed.nextInt(this.height);

            for(int j = 0; j < 10; ++j) {
                int flowerX = widthSeed;
                int flowerZ = heightSeed;

                for(int k = 0; k < 5; ++k) {
                    flowerX += seed.nextInt(6) - seed.nextInt(6);
                    flowerZ += seed.nextInt(6) - seed.nextInt(6);
                    if(flowerX >= 0 && flowerZ >= 0 && flowerX < this.width && flowerZ < this.height) {
                        int i12 = blocks[flowerX + flowerZ * 256] + 1;
                        if((this.blocks[(i12 * this.height + flowerZ) * this.width + flowerX] & 255) == 0) {
                            int i13 = (i12 * this.height + flowerZ) * this.width + flowerX;

                            if(rose == 0) {
                                this.blocks[i13] = (byte)Block.YELLOW_FLOWER.id;
                            } else {
                                this.blocks[i13] = (byte)Block.RED_FLOWER.id;
                            }
                        }
                    }
                }
            }
        }
    }

    private void randomIncOre(int rarity) {
        int rarityInt = 256 * rarity / 100;

        for(int i = 0; i < rarityInt; ++i) {
            seed.nextFloat();
            seed.nextFloat();
            seed.nextFloat();
            int localInt = (int)((seed.nextFloat() + seed.nextFloat()) * 75.0F * (float)rarity / 100.0F);
            seed.nextFloat();
            seed.nextFloat();

            for(int j = 0; j < localInt; ++j) {
                seed.nextFloat();
                seed.nextFloat();
                seed.nextFloat();
                seed.nextFloat();
            }
        }

    }
}