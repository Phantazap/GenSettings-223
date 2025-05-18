package com.phantazap.gensettings.client;

import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.client.ProgressListener;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.NaturalSpawner;
import net.minecraft.world.World;
import net.minecraft.world.gen.noise.Distort;
import net.minecraft.world.gen.noise.Noise;
import net.minecraft.world.gen.noise.PerlinNoise;

import java.util.ArrayList;
import java.util.Random;

public class CGWorldGenerator{
    public ProgressListener listener;
    public int sizeX;
    public int sizeZ;
    public int sizeY;
    public Random seed;
    public byte[] blocks;
    public int surroundingWaterHeight;
    public int surroundingGroundHeight;
    public boolean island = false;
    public boolean floating = false;
    public boolean flat = false;
    public int theme;
    public int phase;
    public int phaseCount;
    public float f_1231443 = 0.0F;
    public int[] f_6516890 = new int[1048576];

    public CGWorldGenerator(ProgressListener listener) {
        this.listener = listener;
    }

    public final World generate(String username, int sizeX, int sizeZ, int sizeY, Random seed, int surroundingWaterHeight, int surroundingGroundHeight) {
        //Has to do with how high terrain can generate
        int var5 = 1;
        if (this.floating) {
            var5 = (sizeY - 64) / 48 + 1;
        }

        this.phaseCount = 13 + var5 * 4;
        this.listener.progressStart("Generating level");
        World var6;
        (var6 = new World()).surroundingWaterHeight = this.surroundingWaterHeight;
        var6.surroundingGroundHeight = this.surroundingGroundHeight;

        this.sizeX = sizeX;
        this.sizeZ = sizeZ;
        this.sizeY = sizeY;
        this.seed = seed;
        this.surroundingWaterHeight = surroundingWaterHeight;
        this.surroundingGroundHeight = surroundingGroundHeight;

        this.blocks = new byte[sizeX * sizeZ * sizeY];

        for(int var7 = 0; var7 < var5; ++var7) {
            this.surroundingWaterHeight = sizeY - 32 - var7 * 48;
            this.surroundingGroundHeight = this.surroundingWaterHeight - 2;
            int[] var8;
            if (this.flat) {
                var8 = new int[sizeX * sizeZ];

                for(int var57 = 0; var57 < var8.length; ++var57) {
                    var8[var57] = 0;
                }

                this.nextPhase();
                this.nextPhase();
            } else {
                this.listener.progressStage("Raising..");
                this.nextPhase();
                CGWorldGenerator var9 = this;
                Distort var10 = new Distort(new PerlinNoise(seed, 8), new PerlinNoise(seed, 8));
                Distort var11 = new Distort(new PerlinNoise(seed, 8), new PerlinNoise(seed, 8));
                PerlinNoise var12 = new PerlinNoise(seed, 6);
                PerlinNoise var13 = new PerlinNoise(seed, 2);
                int[] var14 = new int[this.sizeX * this.sizeZ];

                for(int var22 = 0; var22 < var9.sizeX; ++var22) {
                    double var23 = Math.abs(((double)var22 / ((double)var9.sizeX - (double)1.0F) - (double)0.5F) * (double)2.0F);
                    var9.phaseProgress((float)var22 * 100.0F / (float)(var9.sizeX - 1));

                    for(int var25 = 0; var25 < var9.sizeZ; ++var25) {
                        double var26 = Math.abs(((double)var25 / ((double)var9.sizeZ - (double)1.0F) - (double)0.5F) * (double)2.0F);
                        double var28 = ((Noise)var10).getValue((double)((float)var22 * 1.3F), (double)((float)var25 * 1.3F)) / (double)6.0F + (double)-4.0F;
                        double var30 = ((Noise)var11).getValue((double)((float)var22 * 1.3F), (double)((float)var25 * 1.3F)) / (double)5.0F + (double)10.0F + (double)-4.0F;
                        if (((Noise)var12).getValue((double)var22, (double)var25) / (double)8.0F > (double)0.0F) {
                            var30 = var28;
                        }

                        double var34 = Math.max(var28, var30) / (double)2.0F;
                        if (var9.island) {
                            double var36 = Math.sqrt(var23 * var23 + var26 * var26) * (double)1.2F;
                            double var39 = ((Noise)var13).getValue((double)((float)var22 * 0.05F), (double)((float)var25 * 0.05F)) / (double)4.0F + (double)1.0F;
                            if ((var36 = Math.max(Math.min(var36, var39), Math.max(var23, var26))) > (double)1.0F) {
                                var36 = (double)1.0F;
                            }

                            if (var36 < (double)0.0F) {
                                var36 = (double)0.0F;
                            }

                            var36 *= var36;
                            if ((var34 = var34 * ((double)1.0F - var36) - var36 * (double)10.0F + (double)5.0F) < (double)0.0F) {
                                var34 -= var34 * var34 * (double)0.2F;
                            }
                        } else if (var34 < (double)0.0F) {
                            var34 *= 0.8;
                        }

                        var14[var22 + var25 * var9.sizeX] = (int)var34;
                    }
                }

                var8 = var14;
                this.listener.progressStage("Eroding..");
                this.nextPhase();
                int[] var62 = var14;
                var9 = this;
                var11 = new Distort(new PerlinNoise(seed, 8), new PerlinNoise(seed, 8));
                Distort var69 = new Distort(new PerlinNoise(seed, 8), new PerlinNoise(seed, 8));

                for(int var73 = 0; var73 < var9.sizeX; ++var73) {
                    var9.phaseProgress((float)var73 * 100.0F / (float)(var9.sizeX - 1));

                    for(int var77 = 0; var77 < var9.sizeZ; ++var77) {
                        double var20 = ((Noise)var11).getValue((double)(var73 << 1), (double)(var77 << 1)) / (double)8.0F;
                        int var86 = ((Noise)var69).getValue((double)(var73 << 1), (double)(var77 << 1)) > (double)0.0F ? 1 : 0;
                        if (var20 > (double)2.0F) {
                            int var90 = ((var62[var73 + var77 * var9.sizeX] - var86) / 2 << 1) + var86;
                            var62[var73 + var77 * var9.sizeX] = var90;
                        }
                    }
                }
            }

            this.listener.progressStage("Soiling..");
            this.nextPhase();
            int[] var63 = var8;
            CGWorldGenerator var58 = this;
            int var67 = this.sizeX;
            int var70 = this.sizeZ;
            int var74 = this.sizeY;
            PerlinNoise var78 = new PerlinNoise(seed, 8);
            PerlinNoise var81 = new PerlinNoise(seed, 8);

            for(int var21 = 0; var21 < var67; ++var21) {
                double var87 = Math.abs(((double)var21 / ((double)var67 - (double)1.0F) - (double)0.5F) * (double)2.0F);
                var58.phaseProgress((float)var21 * 100.0F / (float)(var67 - 1));

                for(int var24 = 0; var24 < var70; ++var24) {
                    double var95 = Math.abs(((double)var24 / ((double)var70 - (double)1.0F) - (double)0.5F) * (double)2.0F);
                    double var27;
                    var27 = (var27 = Math.max(var87, var95)) * var27 * var27;
                    int var29 = (int)(((Noise)var78).getValue((double)var21, (double)var24) / (double)24.0F) - 4;
                    int var108;
                    int var31 = (var108 = var63[var21 + var24 * var67] + var58.surroundingWaterHeight) + var29;
                    var63[var21 + var24 * var67] = Math.max(var108, var31);
                    if (var63[var21 + var24 * var67] > var74 - 2) {
                        var63[var21 + var24 * var67] = var74 - 2;
                    }

                    if (var63[var21 + var24 * var67] <= 0) {
                        var63[var21 + var24 * var67] = 1;
                    }

                    double var32;
                    int var112;
                    if ((var112 = (int)((double)((int)(Math.sqrt(Math.abs(var32 = ((Noise)var81).getValue((double)var21 * 2.3, (double)var24 * 2.3) / (double)24.0F)) * Math.signum(var32) * (double)20.0F) + var58.surroundingWaterHeight) * ((double)1.0F - var27) + var27 * (double)var58.sizeY)) > var58.surroundingWaterHeight) {
                        var112 = var58.sizeY;
                    }

                    for(int var35 = 0; var35 < var74; ++var35) {
                        int var117 = (var35 * var58.sizeZ + var24) * var58.sizeX + var21;
                        int var37 = 0;
                        if (var35 <= var108) {
                            var37 = Block.DIRT.id;
                        }

                        if (var35 <= var31) {
                            var37 = Block.STONE.id;
                        }

                        if (var58.floating && var35 < var112) {
                            var37 = 0;
                        }

                        if (var58.blocks[var117] == 0) {
                            var58.blocks[var117] = (byte)var37;
                        }
                    }
                }
            }

            this.listener.progressStage("Growing..");
            this.nextPhase();
            var63 = var8;
            var58 = this;
            var67 = this.sizeX;
            var70 = this.sizeZ;
            PerlinNoise var75 = new PerlinNoise(seed, 8);
            var78 = new PerlinNoise(seed, 8);
            int var82 = this.surroundingWaterHeight - 1;
            if (this.theme == 2) {
                var82 += 2;
            }

            for(int var84 = 0; var84 < var67; ++var84) {
                var58.phaseProgress((float)var84 * 100.0F / (float)(var67 - 1));

                for(int var88 = 0; var88 < var70; ++var88) {
                    boolean var91 = ((Noise)var75).getValue((double)var84, (double)var88) > (double)8.0F;
                    if (var58.island) {
                        var91 = ((Noise)var75).getValue((double)var84, (double)var88) > (double)-8.0F;
                    }

                    if (var58.theme == 2) {
                        var91 = ((Noise)var75).getValue((double)var84, (double)var88) > (double)-32.0F;
                    }

                    boolean var93 = ((Noise)var78).getValue((double)var84, (double)var88) > (double)12.0F;
                    if (var58.theme == 1 || var58.theme == 3) {
                        var91 = ((Noise)var75).getValue((double)var84, (double)var88) > (double)-8.0F;
                    }

                    int var96;
                    int var98 = ((var96 = var63[var84 + var88 * var67]) * var58.sizeZ + var88) * var58.sizeX + var84;
                    int var101;
                    if (((var101 = var58.blocks[((var96 + 1) * var58.sizeZ + var88) * var58.sizeX + var84] & 255) == Block.FLOWING_WATER.id || var101 == Block.WATER.id || var101 == 0) && var96 <= var58.surroundingWaterHeight - 1 && var93) {
                        var58.blocks[var98] = (byte)Block.GRAVEL.id;
                    }

                    if (var101 == 0) {
                        int var104 = -1;
                        if (var96 <= var82 && var91) {
                            var104 = Block.SAND.id;
                            if (var58.theme == 1) {
                                var104 = Block.GRASS.id;
                            }
                        }

                        if (var58.blocks[var98] != 0 && var104 > 0) {
                            var58.blocks[var98] = (byte)var104;
                        }
                    }
                }
            }
        }

        this.listener.progressStage("Carving..");
        this.nextPhase();
        CGWorldGenerator var60 = this;
        int var72 = this.sizeX;
        int var76 = this.sizeZ;
        int var80 = this.sizeY;
        int var83 = var72 * var76 * var80 / 256 / 64 << 1;

        for(int var85 = 0; var85 < var83; ++var85) {
            var60.phaseProgress((float)var85 * 100.0F / (float)(var83 - 1));
            float var89 = var60.seed.nextFloat() * (float)var72;
            float var92 = var60.seed.nextFloat() * (float)var80;
            float var94 = var60.seed.nextFloat() * (float)var76;
            int var97 = (int)((var60.seed.nextFloat() + var60.seed.nextFloat()) * 200.0F);
            float var99 = var60.seed.nextFloat() * (float)Math.PI * 2.0F;
            float var102 = 0.0F;
            float var105 = var60.seed.nextFloat() * (float)Math.PI * 2.0F;
            float var106 = 0.0F;
            float var109 = var60.seed.nextFloat() * var60.seed.nextFloat();

            for(int var110 = 0; var110 < var97; ++var110) {
                var89 += MathHelper.sin(var99) * MathHelper.cos(var105);
                var94 += MathHelper.cos(var99) * MathHelper.cos(var105);
                var92 += MathHelper.sin(var105);
                var99 += var102 * 0.2F;
                float var103;
                var102 = (var103 = var102 * 0.9F) + (var60.seed.nextFloat() - var60.seed.nextFloat());
                var105 = (var105 + var106 * 0.5F) * 0.5F;
                float var107;
                var106 = (var107 = var106 * 0.75F) + (var60.seed.nextFloat() - var60.seed.nextFloat());
                if (!(var60.seed.nextFloat() < 0.25F)) {
                    float var111 = var89 + (var60.seed.nextFloat() * 4.0F - 2.0F) * 0.2F;
                    float var33 = var92 + (var60.seed.nextFloat() * 4.0F - 2.0F) * 0.2F;
                    float var113 = var94 + (var60.seed.nextFloat() * 4.0F - 2.0F) * 0.2F;
                    float var114 = ((float)var60.sizeY - var33) / (float)var60.sizeY;
                    float var118 = 1.2F + (var114 * 3.5F + 1.0F) * var109;
                    float var119 = MathHelper.sin((float)var110 * (float)Math.PI / (float)var97) * var118;

                    for(int var42 = (int)(var111 - var119); var42 <= (int)(var111 + var119); ++var42) {
                        for(int var120 = (int)(var33 - var119); var120 <= (int)(var33 + var119); ++var120) {
                            for(int var40 = (int)(var113 - var119); var40 <= (int)(var113 + var119); ++var40) {
                                float var47 = (float)var42 - var111;
                                float var54 = (float)var120 - var33;
                                float var65 = (float)var40 - var113;
                                if (var47 * var47 + var54 * var54 * 2.0F + var65 * var65 < var119 * var119 && var42 > 0 && var120 > 0 && var40 > 0 && var42 < var60.sizeX - 1 && var120 < var60.sizeY - 1 && var40 < var60.sizeZ - 1) {
                                    int var48 = (var120 * var60.sizeZ + var40) * var60.sizeX + var42;
                                    if (var60.blocks[var48] == Block.STONE.id) {
                                        var60.blocks[var48] = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        int var49 = this.placeOre(Block.COAL_ORE.id, 1000, 10, (sizeY << 2) / 5);
        int var55 = this.placeOre(Block.IRON_ORE.id, 800, 8, sizeY * 3 / 5);
        int var61 = this.placeOre(Block.GOLD_ORE.id, 500, 6, (sizeY << 1) / 5);
        var5 = this.placeOre(Block.DIAMOND_ORE.id, 800, 2, sizeY / 5);
        System.out.println("Coal: " + var49 + ", Iron: " + var55 + ", Gold: " + var61 + ", Diamond: " + var5);
        this.listener.progressStage("Melting..");
        this.nextPhase();
        this.placeUndergroundLakes();
        var6.cloudHeight = sizeY + 2;
        if (this.floating) {
            this.surroundingGroundHeight = -128;
            this.surroundingWaterHeight = this.surroundingGroundHeight + 1;
            var6.cloudHeight = -16;
        } else if (!this.island) {
            this.surroundingGroundHeight = this.surroundingWaterHeight + 1;
            this.surroundingWaterHeight = this.surroundingGroundHeight - 16;
        } else {
            this.surroundingGroundHeight = this.surroundingWaterHeight - 9;
        }

        this.listener.progressStage("Watering..");
        this.nextPhase();
        this.placeLakes();
        if (!this.floating) {
            var5 = Block.WATER.id;
            if (this.theme == 1) {
                var5 = Block.LAVA.id;
            }

            for(int var50 = 0; var50 < sizeX; ++var50) {
                this.placeLake(var50, this.surroundingWaterHeight - 1, 0, 0, var5);
                this.placeLake(var50, this.surroundingWaterHeight - 1, sizeZ - 1, 0, var5);
            }

            for(int var51 = 0; var51 < sizeZ; ++var51) {
                this.placeLake(sizeX - 1, this.surroundingWaterHeight - 1, var51, 0, var5);
                this.placeLake(0, this.surroundingWaterHeight - 1, var51, 0, var5);
            }
        }

        if (this.theme == 0) {
            var6.skyColor = 10079487;
            var6.fogColor = 16777215;
            var6.cloudColor = 16777215;
        }

        if (this.theme == 1) {
            var6.cloudColor = 2164736;
            var6.fogColor = 1049600;
            var6.skyColor = 1049600;
            var6.sunLight = var6.skyBrightness = 7;
            var6.surroundingWaterBlock = Block.FLOWING_LAVA.id;
            if (this.floating) {
                var6.cloudHeight = sizeY + 2;
                this.surroundingWaterHeight = -16;
            }
        }

        if (this.theme == 2) {
            var6.skyColor = 13033215;
            var6.fogColor = 13033215;
            var6.cloudColor = 15658751;
            var6.sunLight = var6.skyBrightness = 15;
            var6.skyBrightness = 16;
            var6.cloudHeight = sizeY + 64;
        }

        if (this.theme == 3) {
            var6.skyColor = 7699847;
            var6.fogColor = 5069403;
            var6.cloudColor = 5069403;
            var6.sunLight = var6.skyBrightness = 12;
        }

        var6.surroundingWaterHeight = this.surroundingWaterHeight;
        var6.surroundingGroundHeight = this.surroundingGroundHeight;
        this.listener.progressStage("Assembling..");
        this.nextPhase();
        this.phaseProgress(0.0F);
        var6.assemble(sizeX, sizeY, sizeZ, this.blocks, (byte[])null);
        this.listener.progressStage("Building..");
        this.nextPhase();
        this.phaseProgress(0.0F);
        var6.findSpawnPoint();
        placeSpawnBuilding(var6);
        this.listener.progressStage("Planting..");
        this.nextPhase();
        if (this.theme != 1) {
            this.placeGrass(var6);
        }

        this.nextPhase();
        this.placeTrees(var6);
        if (this.theme == 3) {
            for(int var45 = 0; var45 < 50; ++var45) {
                this.placeTrees(var6);
            }
        }

        var5 = 100;
        if (this.theme == 2) {
            var5 = 1000;
        }

        this.nextPhase();
        this.placePlant(var6, Block.YELLOW_FLOWER, var5);
        this.nextPhase();
        this.placePlant(var6, Block.RED_FLOWER, var5);
        this.nextPhase();
        this.placePlant(var6, Block.BROWN_MUSHROOM, 50);
        this.nextPhase();
        this.placePlant(var6, Block.RED_MUSHROOM, 50);
        this.listener.progressStage("Lighting..");
        this.nextPhase();

        for(int var52 = 0; var52 < 10000; ++var52) {
            this.phaseProgress((float)(var52 * 100 / 10000));
            var6.updateLight();
        }

        this.listener.progressStage("Spawning..");
        this.nextPhase();
        NaturalSpawner var53 = new NaturalSpawner(var6);

        for(int var41 = 0; var41 < 1000; ++var41) {
            this.phaseProgress((float)var41 * 100.0F / 999.0F);
            var53.tick();
        }

        var6.timeOfCreation = System.currentTimeMillis();
        var6.author = username;
        var6.name = "A Nice World";
        if (this.phase != this.phaseCount) {
            throw new IllegalStateException("Wrong number of phases! Wanted " + this.phaseCount + ", got " + this.phase);
        } else {
            return var6;
        }
    }

    private static void placeSpawnBuilding(World world) {
        int var1 = world.spawnX;
        int var2 = world.spawnY;
        int var3 = world.spawnZ;

        for(int var4 = var1 - 3; var4 <= var1 + 3; ++var4) {
            for(int var5 = var2 - 2; var5 <= var2 + 2; ++var5) {
                for(int var6 = var3 - 3; var6 <= var3 + 3; ++var6) {
                    int var7 = var5 < var2 - 1 ? Block.OBSIDIAN.id : 0;
                    if (var4 == var1 - 3 || var6 == var3 - 3 || var4 == var1 + 3 || var6 == var3 + 3 || var5 == var2 - 2 || var5 == var2 + 2) {
                        var7 = Block.STONE.id;
                        if (var5 >= var2 - 1) {
                            var7 = Block.PLANKS.id;
                        }
                    }

                    if (var6 == var3 - 3 && var4 == var1 && var5 >= var2 - 1 && var5 <= var2) {
                        var7 = 0;
                    }

                    world.setBlock(var4, var5, var6, var7);
                }
            }
        }

        world.setBlock(var1 - 3 + 1, var2, var3, Block.TORCH.id);
        world.setBlock(var1 + 3 - 1, var2, var3, Block.TORCH.id);
    }

    private void placeGrass(World world) {
        for(int var2 = 0; var2 < this.sizeX; ++var2) {
            this.phaseProgress((float)var2 * 100.0F / (float)(this.sizeX - 1));

            for(int var3 = 0; var3 < this.sizeY; ++var3) {
                for(int var4 = 0; var4 < this.sizeZ; ++var4) {
                    if (world.getBlock(var2, var3, var4) == Block.DIRT.id && world.getRawBrightness(var2, var3 + 1, var4) >= 4 && !world.getMaterial(var2, var3 + 1, var4).isOpaque()) {
                        world.setBlockQuietly(var2, var3, var4, Block.GRASS.id);
                    }
                }
            }
        }

    }

    private void placeTrees(World world) {
        int var2 = this.sizeX * this.sizeZ * this.sizeY / 80000;

        for(int var3 = 0; var3 < var2; ++var3) {
            if (var3 % 100 == 0) {
                this.phaseProgress((float)var3 * 100.0F / (float)(var2 - 1));
            }

            int var4 = this.seed.nextInt(this.sizeX);
            int var5 = this.seed.nextInt(this.sizeY);
            int var6 = this.seed.nextInt(this.sizeZ);

            for(int var7 = 0; var7 < 25; ++var7) {
                int var8 = var4;
                int var9 = var5;
                int var10 = var6;

                for(int var11 = 0; var11 < 20; ++var11) {
                    var8 += this.seed.nextInt(12) - this.seed.nextInt(12);
                    var9 += this.seed.nextInt(3) - this.seed.nextInt(6);
                    var10 += this.seed.nextInt(12) - this.seed.nextInt(12);
                    if (var8 >= 0 && var9 >= 0 && var10 >= 0 && var8 < this.sizeX && var9 < this.sizeY && var10 < this.sizeZ) {
                        world.placeTree(var8, var9, var10);
                    }
                }
            }
        }

    }

    private void placePlant(World world, PlantBlock plant, int chance) {
        chance = (int)((long)this.sizeX * (long)this.sizeZ * (long)this.sizeY * (long)chance / 1600000L);

        for(int var4 = 0; var4 < chance; ++var4) {
            if (var4 % 100 == 0) {
                this.phaseProgress((float)var4 * 100.0F / (float)(chance - 1));
            }

            int var5 = this.seed.nextInt(this.sizeX);
            int var6 = this.seed.nextInt(this.sizeY);
            int var7 = this.seed.nextInt(this.sizeZ);

            for(int var8 = 0; var8 < 10; ++var8) {
                int var9 = var5;
                int var10 = var6;
                int var11 = var7;

                for(int var12 = 0; var12 < 10; ++var12) {
                    var9 += this.seed.nextInt(4) - this.seed.nextInt(4);
                    var10 += this.seed.nextInt(2) - this.seed.nextInt(2);
                    var11 += this.seed.nextInt(4) - this.seed.nextInt(4);
                    if (var9 >= 0 && var11 >= 0 && var10 > 0 && var9 < this.sizeX && var11 < this.sizeZ && var10 < this.sizeY && world.getBlock(var9, var10, var11) == 0 && plant.canSurvive(world, var9, var10, var11)) {
                        world.setBlock(var9, var10, var11, plant.id);
                    }
                }
            }
        }

    }

    private int placeOre(int ore, int chance, int minY, int maxY) {
        int var5 = 0;
        ore = (byte)ore;
        int var6 = this.sizeX;
        int var7 = this.sizeZ;
        int var8 = this.sizeY;
        chance = var6 * var7 * var8 / 256 / 64 * chance / 100;

        for(int var9 = 0; var9 < chance; ++var9) {
            this.phaseProgress((float)var9 * 100.0F / (float)(chance - 1));
            float var10 = this.seed.nextFloat() * (float)var6;
            float var11 = this.seed.nextFloat() * (float)var8;
            float var12 = this.seed.nextFloat() * (float)var7;
            if (!(var11 > (float)maxY)) {
                int var13 = (int)((this.seed.nextFloat() + this.seed.nextFloat()) * 75.0F * (float)minY / 100.0F);
                float var14 = this.seed.nextFloat() * (float)Math.PI * 2.0F;
                float var15 = 0.0F;
                float var16 = this.seed.nextFloat() * (float)Math.PI * 2.0F;
                float var17 = 0.0F;

                for(int var18 = 0; var18 < var13; ++var18) {
                    var10 += MathHelper.sin(var14) * MathHelper.cos(var16);
                    var12 += MathHelper.cos(var14) * MathHelper.cos(var16);
                    var11 += MathHelper.sin(var16);
                    var14 += var15 * 0.2F;
                    float var28;
                    var15 = (var28 = var15 * 0.9F) + (this.seed.nextFloat() - this.seed.nextFloat());
                    var16 = (var16 + var17 * 0.5F) * 0.5F;
                    float var29;
                    var17 = (var29 = var17 * 0.9F) + (this.seed.nextFloat() - this.seed.nextFloat());
                    float var19 = MathHelper.sin((float)var18 * (float)Math.PI / (float)var13) * (float)minY / 100.0F + 1.0F;

                    for(int var20 = (int)(var10 - var19); var20 <= (int)(var10 + var19); ++var20) {
                        for(int var21 = (int)(var11 - var19); var21 <= (int)(var11 + var19); ++var21) {
                            for(int var22 = (int)(var12 - var19); var22 <= (int)(var12 + var19); ++var22) {
                                float var23 = (float)var20 - var10;
                                float var24 = (float)var21 - var11;
                                float var25 = (float)var22 - var12;
                                if (var23 * var23 + var24 * var24 * 2.0F + var25 * var25 < var19 * var19 && var20 > 0 && var21 > 0 && var22 > 0 && var20 < this.sizeX - 1 && var21 < this.sizeY - 1 && var22 < this.sizeZ - 1) {
                                    int var30 = (var21 * this.sizeZ + var22) * this.sizeX + var20;
                                    if (this.blocks[var30] == Block.STONE.id) {
                                        this.blocks[var30] = (byte)ore;
                                        ++var5;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return var5;
    }

    private void placeLakes() {
        int var1 = Block.WATER.id;
        if (this.theme == 1) {
            var1 = Block.LAVA.id;
        }

        int var2 = this.sizeX * this.sizeZ * this.sizeY / 1000;

        for(int var3 = 0; var3 < var2; ++var3) {
            if (var3 % 100 == 0) {
                this.phaseProgress((float)var3 * 100.0F / (float)(var2 - 1));
            }

            int var4 = this.seed.nextInt(this.sizeX);
            int var5 = this.seed.nextInt(this.sizeY);
            int var6 = this.seed.nextInt(this.sizeZ);
            if (this.blocks[(var5 * this.sizeZ + var6) * this.sizeX + var4] == 0) {
                long var7;
                if ((var7 = this.placeLake(var4, var5, var6, 0, 255)) > 0L && var7 < 640L) {
                    this.placeLake(var4, var5, var6, 255, var1);
                } else {
                    this.placeLake(var4, var5, var6, 255, 0);
                }
            }
        }

        this.phaseProgress(100.0F);
    }

    private void nextPhase() {
        ++this.phase;
        this.f_1231443 = 0.0F;
        this.phaseProgress(0.0F);
    }

    private void phaseProgress(float progress) {
        if (progress < 0.0F) {
            throw new IllegalStateException("Failed to set next phase!");
        } else {
            int var2 = (int)(((float)(this.phase - 1) + progress / 100.0F) * 100.0F / (float)this.phaseCount);
            this.listener.progressPercentage(var2);
        }
    }

    private void placeUndergroundLakes() {
        int var1 = this.sizeX * this.sizeZ * this.sizeY / 2000;
        int var2 = this.surroundingGroundHeight;

        for(int var3 = 0; var3 < var1; ++var3) {
            if (var3 % 100 == 0) {
                this.phaseProgress((float)var3 * 100.0F / (float)(var1 - 1));
            }

            int var4 = this.seed.nextInt(this.sizeX);
            int var5 = Math.min(Math.min(this.seed.nextInt(var2), this.seed.nextInt(var2)), Math.min(this.seed.nextInt(var2), this.seed.nextInt(var2)));
            int var6 = this.seed.nextInt(this.sizeZ);
            if (this.blocks[(var5 * this.sizeZ + var6) * this.sizeX + var4] == 0) {
                long var7;
                if ((var7 = this.placeLake(var4, var5, var6, 0, 255)) > 0L && var7 < 640L) {
                    this.placeLake(var4, var5, var6, 255, Block.LAVA.id);
                } else {
                    this.placeLake(var4, var5, var6, 255, 0);
                }
            }
        }

        this.phaseProgress(100.0F);
    }

    private long placeLake(int x, int z, int y, int maxY, int liquid) {
        byte var6 = (byte)liquid;
        maxY = (byte)maxY;
        ArrayList var7 = new ArrayList();
        int var8 = 0;
        int var9 = 1;

        int var10;
        for(var10 = 1; 1 << var9 < this.sizeX; ++var9) {
        }

        while(1 << var10 < this.sizeZ) {
            ++var10;
        }

        int var11 = this.sizeZ - 1;
        int var12 = this.sizeX - 1;
        ++var8;
        this.f_6516890[0] = ((z << var10) + y << var9) + x;
        long var14 = 0L;
        x = this.sizeX * this.sizeZ;

        while(var8 > 0) {
            --var8;
            z = this.f_6516890[var8];
            if (var8 == 0 && var7.size() > 0) {
                this.f_6516890 = (int[])var7.remove(var7.size() - 1);
                var8 = this.f_6516890.length;
            }

            y = z >> var9 & var11;
            int var13 = z >> var9 + var10;

            int var16;
            int var17;
            for(var17 = var16 = z & var12; var16 > 0 && this.blocks[z - 1] == maxY; --z) {
                --var16;
            }

            while(var17 < this.sizeX && this.blocks[z + var17 - var16] == maxY) {
                ++var17;
            }

            int var18 = z >> var9 & var11;
            int var19 = z >> var9 + var10;
            if (liquid == 255 && (var16 == 0 || var17 == this.sizeX - 1 || var13 == 0 || var13 == this.sizeY - 1 || y == 0 || y == this.sizeZ - 1)) {
                return -1L;
            }

            if (var18 != y || var19 != var13) {
                System.out.println("Diagonal flood!?");
            }

            boolean var18_2 = false;
            boolean var19_2 = false;
            boolean var20 = false;
            var14 += (long)(var17 - var16);

            for(int var27 = var16; var27 < var17; ++var27) {
                this.blocks[z] = var6;
                if (y > 0) {
                    boolean var21;
                    if ((var21 = this.blocks[z - this.sizeX] == maxY) && !var18_2) {
                        if (var8 == this.f_6516890.length) {
                            var7.add(this.f_6516890);
                            this.f_6516890 = new int[1048576];
                            var8 = 0;
                        }

                        this.f_6516890[var8++] = z - this.sizeX;
                    }

                    var18_2 = var21;
                }

                if (y < this.sizeZ - 1) {
                    boolean var30;
                    if ((var30 = this.blocks[z + this.sizeX] == maxY) && !var19_2) {
                        if (var8 == this.f_6516890.length) {
                            var7.add(this.f_6516890);
                            this.f_6516890 = new int[1048576];
                            var8 = 0;
                        }

                        this.f_6516890[var8++] = z + this.sizeX;
                    }

                    var19_2 = var30;
                }

                if (var13 > 0) {
                    byte var31 = this.blocks[z - x];
                    if ((var6 == Block.FLOWING_LAVA.id || var6 == Block.LAVA.id) && (var31 == Block.FLOWING_WATER.id || var31 == Block.WATER.id)) {
                        this.blocks[z - x] = (byte)Block.STONE.id;
                    }

                    if ((var31 == maxY) && !var20) {
                        if (var8 == this.f_6516890.length) {
                            var7.add(this.f_6516890);
                            this.f_6516890 = new int[1048576];
                            var8 = 0;
                        }

                        this.f_6516890[var8++] = z - x;
                    }
                    
                }

                ++z;
            }
        }

        return var14;
    }
}
