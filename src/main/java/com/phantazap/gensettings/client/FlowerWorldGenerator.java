package com.phantazap.gensettings.client;

import net.minecraft.client.ProgressListener;
import net.minecraft.world.World;

public class FlowerWorldGenerator {
    private final ProgressListener levelLoaderListener;
    private int width;
    private int height;
    public SkippableRandom seed;
    private byte[] blocks;

    public FlowerWorldGenerator(ProgressListener levelLoaderListener) {
        this.levelLoaderListener = levelLoaderListener;
    }

    public World generateLevel(int width, int length, int height) {
        //Do some initializing stuff
        long start = System.currentTimeMillis();
        this.levelLoaderListener.progressStart("Generating level");
        this.width = width;
        this.height = length;
        this.seed = new SkippableRandom(2048);
        int waterLevel = 1;
        this.blocks = new byte[width * length << 6];
        this.levelLoaderListener.progressStage("Raising..");
        int[] plane = new int[256 * 256];

        //Necessary to avoid crash
        for (int widthLoop = 0; widthLoop < 256; ++widthLoop) {
            for (int lengthLoop = 0; lengthLoop < 256; ++lengthLoop) {
                int area = plane[widthLoop + lengthLoop * 256] + waterLevel;
                plane[widthLoop + lengthLoop * 256] = area;
            }
        }

        //Advances the seed as many times as it would in the full generator to keep our flowers consistent.
        // Instead of creating new instances of perlin noise, and improved noise, skip random by 256 * levels.
        this.seed.skip(19968); // (256 * 78)

        for (int i = 0; i < 512; ++i) {
            this.seed.skip(3L);
            int localFloat = (int)((this.seed.nextFloat() + this.seed.nextFloat()) * 200.0F);
            this.seed.skip(4L);

            for (int j = 0; j < localFloat; ++j) {
                this.seed.skip(4L);
                if (this.seed.nextFloat() >= 0.25F) {
                    this.seed.skip(3L);
                }
            }
        }

        this.randomIncOre(90);
        this.randomIncOre(70);
        this.randomIncOre(50);

        this.seed.skip(4956); // (3 * 8) + (4 * 209) + (256 * 16)

        //Grow our flowers
        this.growFlowers(plane);

        //Save settings
        World world = new World();
        world.surroundingWaterHeight = waterLevel;
        world.surroundingGroundHeight = waterLevel;
        world.assemble(width, 64, length, this.blocks, null);
        world.timeOfCreation = System.currentTimeMillis();
        world.author = "Phantazap";
        world.name = "A Nice World";
        world.spawnY = 2;
        world.cloudHeight = height + 2;
        this.levelLoaderListener.progressStage("Spawning..");
        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        System.out.println("World generated in " + timeElapsed + " milliseconds.");
        return world;
    }

    private void growFlowers(int[] blocks) {
        for (int i = 0; i < 21; ++i) {
            int rose = this.seed.nextInt(2);
            int widthSeed = this.seed.nextInt(this.width);
            int heightSeed = this.seed.nextInt(this.height);
            for (int j = 0; j < 10; ++j) {
                int flowerX = widthSeed;
                int flowerZ = heightSeed;
                for (int k = 0; k < 5; ++k) {
                    flowerX += this.seed.nextInt(6) - this.seed.nextInt(6);
                    flowerZ += this.seed.nextInt(6) - this.seed.nextInt(6);
                    if (flowerX >= 0 && flowerZ >= 0 && flowerX < this.width && flowerZ < this.height) {
                        int index = ((blocks[flowerX + flowerZ * 256] + 1) * this.height + flowerZ) * this.width + flowerX;
                        if ((this.blocks[index] & 255) == 0) {
                            this.blocks[index] = (byte) (rose == 0 ? 37 : 38); // Block.YELLOW_FLOWER.id : Block.RED_FLOWER.id
                        }
                    }
                }
            }
        }
    }

    private void randomIncOre(int rarity) {
        for (int i = 0; i < (256 * rarity / 100); i++) {
            this.seed.skip(3L);
            int localInt = (int)((this.seed.nextFloat() + this.seed.nextFloat()) * 75.0F * (float)rarity / 100.0F);
            this.seed.skip(2L);
            this.seed.skip(4L * localInt);
        }
    }
}