package com.phantazap.gensettings.client;

import net.minecraft.entity.Entity;
import net.minecraft.client.ProgressListener;
import net.minecraft.world.World;
import net.minecraft.world.gen.noise.Distort;
import net.minecraft.world.gen.noise.PerlinNoise;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Random;

public class ClassicLevelGenerator {
    private ProgressListener levelLoaderListener;
    private int width;
    private int height;
    private int depth;
    public Random seed;
    private byte[] blocks;
    private int waterLevel;
    private int[] coords = new int[1048576];

    public ClassicLevelGenerator(ProgressListener levelLoaderListener) {
        this.levelLoaderListener = levelLoaderListener;
    }

    public final World generateLevel(String creator, int width, int height, int depth, Random seed) {
        this.levelLoaderListener.progressStart("Generating level");
        this.width = width;
        this.height = height;
        this.depth = 64;
        this.seed = seed;
        this.waterLevel = 32;
        this.blocks = new byte[width * height << 6];
        this.levelLoaderListener.progressStage("Raising..");
        ClassicLevelGenerator levelGen6 = this;
        Distort distort8 = new Distort(new PerlinNoise(seed, 8), new PerlinNoise(seed, 8));
        Distort distort9 = new Distort(new PerlinNoise(seed, 8), new PerlinNoise(seed, 8));
        PerlinNoise perlinNoise10 = new PerlinNoise(seed, 6);
        int[] i11 = new int[this.width * this.height];
        float f5 = 1.3F;

        int i14;
        int i15;
        for(i14 = 0; i14 < levelGen6.width; ++i14) {
            levelGen6.setNextPhase(i14 * 100 / (levelGen6.width - 1));

            for(i15 = 0; i15 < levelGen6.height; ++i15) {
                double d16 = distort8.getValue((double)((float)i14 * f5), (double)((float)i15 * f5)) / 6.0D + (double)-4;
                double d18 = distort9.getValue((double)((float)i14 * f5), (double)((float)i15 * f5)) / 5.0D + 10.0D + (double)-4;
                if(perlinNoise10.getValue((double)i14, (double)i15) / 8.0D > 0.0D) {
                    d18 = d16;
                }

                double d22;
                if((d22 = Math.max(d16, d18) / 2.0D) < 0.0D) {
                    d22 *= 0.8D;
                }

                i11[i14 + i15 * levelGen6.width] = (int)d22;
            }
        }

        this.levelLoaderListener.progressStage("Eroding..");
        int[] i35 = i11;
        levelGen6 = this;
        distort9 = new Distort(new PerlinNoise(seed, 8), new PerlinNoise(seed, 8));
        Distort distort41 = new Distort(new PerlinNoise(seed, 8), new PerlinNoise(seed, 8));

        int i32;
        int i44;
        int i47;
        for(i44 = 0; i44 < levelGen6.width; ++i44) {
            levelGen6.setNextPhase(i44 * 100 / (levelGen6.width - 1));

            for(i32 = 0; i32 < levelGen6.height; ++i32) {
                double d13 = distort9.getValue((double)(i44 << 1), (double)(i32 << 1)) / 8.0D;
                i15 = distort41.getValue((double)(i44 << 1), (double)(i32 << 1)) > 0.0D ? 1 : 0;
                if(d13 > 2.0D) {
                    i47 = ((i35[i44 + i32 * levelGen6.width] - i15) / 2 << 1) + i15;
                    i35[i44 + i32 * levelGen6.width] = i47;
                }
            }
        }

        this.levelLoaderListener.progressStage("Soiling..");
        i35 = i11;
        levelGen6 = this;
        int i38 = this.width;
        int i43 = this.height;
        i44 = this.depth;
        PerlinNoise perlinNoise33 = new PerlinNoise(seed, 8);

        int i17;
        int i46;
        int i52;
        for(i46 = 0; i46 < i38; ++i46) {
            levelGen6.setNextPhase(i46 * 100 / (levelGen6.width - 1));

            for(i14 = 0; i14 < i43; ++i14) {
                i15 = (int)(perlinNoise33.getValue((double)i46, (double)i14) / 24.0D) - 4;
                i17 = (i47 = i35[i46 + i14 * i38] + levelGen6.waterLevel) + i15;
                i35[i46 + i14 * i38] = Math.max(i47, i17);
                if(i35[i46 + i14 * i38] > i44 - 2) {
                    i35[i46 + i14 * i38] = i44 - 2;
                }

                if(i35[i46 + i14 * i38] < 1) {
                    i35[i46 + i14 * i38] = 1;
                }

                for(i52 = 0; i52 < i44; ++i52) {
                    int i19 = (i52 * levelGen6.height + i14) * levelGen6.width + i46;
                    int i20 = 0;
                    if(i52 <= i47) {
                        i20 = Block.DIRT.id;
                    }

                    if(i52 <= i17) {
                        i20 = Block.STONE.id;
                    }

                    if(i52 == 0) {
                        i20 = Block.FLOWING_LAVA.id;
                    }

                    levelGen6.blocks[i19] = (byte)i20;
                }
            }
        }

        this.levelLoaderListener.progressStage("Carving..");
        boolean z40 = true;
        boolean z36 = false;
        levelGen6 = this;
        i43 = this.width;
        i44 = this.height;
        i32 = this.depth;
        i46 = i43 * i44 * i32 / 256 / 64 << 1;

        for(i14 = 0; i14 < i46; ++i14) {
            levelGen6.setNextPhase(i14 * 100 / (i46 - 1) / 4);
            float f48 = levelGen6.seed.nextFloat() * (float)i43;
            float f49 = levelGen6.seed.nextFloat() * (float)i32;
            float f50 = levelGen6.seed.nextFloat() * (float)i44;
            i52 = (int)((levelGen6.seed.nextFloat() + levelGen6.seed.nextFloat()) * 200.0F);
            float f53 = (float)((double)levelGen6.seed.nextFloat() * Math.PI * 2.0D);
            float f54 = 0.0F;
            float f21 = (float)((double)levelGen6.seed.nextFloat() * Math.PI * 2.0D);
            float f55 = 0.0F;
            float f23 = levelGen6.seed.nextFloat() * levelGen6.seed.nextFloat();

            for(int i7 = 0; i7 < i52; ++i7) {
                f48 = (float)((double)f48 + Math.sin((double)f53) * Math.cos((double)f21));
                f50 = (float)((double)f50 + Math.cos((double)f53) * Math.cos((double)f21));
                f49 = (float)((double)f49 + Math.sin((double)f21));
                f53 += f54 * 0.2F;
                f54 = (f54 *= 0.9F) + (levelGen6.seed.nextFloat() - levelGen6.seed.nextFloat());
                f21 = (f21 + f55 * 0.5F) * 0.5F;
                f55 = (f55 *= 0.75F) + (levelGen6.seed.nextFloat() - levelGen6.seed.nextFloat());
                if(levelGen6.seed.nextFloat() >= 0.25F) {
                    float f37 = f48 + (levelGen6.seed.nextFloat() * 4.0F - 2.0F) * 0.2F;
                    float f42 = f49 + (levelGen6.seed.nextFloat() * 4.0F - 2.0F) * 0.2F;
                    float f12 = f50 + (levelGen6.seed.nextFloat() * 4.0F - 2.0F) * 0.2F;
                    float f24 = ((float)levelGen6.depth - f42) / (float)levelGen6.depth;
                    f24 = 1.2F + (f24 * 3.5F + 1.0F) * f23;
                    f24 = (float)(Math.sin((double)i7 * Math.PI / (double)i52) * (double)f24);

                    for(int i25 = (int)(f37 - f24); i25 <= (int)(f37 + f24); ++i25) {
                        for(int i26 = (int)(f42 - f24); i26 <= (int)(f42 + f24); ++i26) {
                            for(int i27 = (int)(f12 - f24); i27 <= (int)(f12 + f24); ++i27) {
                                float f28 = (float)i25 - f37;
                                float f29 = (float)i26 - f42;
                                float f30 = (float)i27 - f12;
                                if(f28 * f28 + f29 * f29 * 2.0F + f30 * f30 < f24 * f24 && i25 >= 1 && i26 >= 1 && i27 >= 1 && i25 < levelGen6.width - 1 && i26 < levelGen6.depth - 1 && i27 < levelGen6.height - 1) {
                                    int i56 = (i26 * levelGen6.height + i27) * levelGen6.width + i25;
                                    if(levelGen6.blocks[i56] == Block.STONE.id) {
                                        levelGen6.blocks[i56] = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        this.addOre(Block.COAL_ORE.id, 90, 1, 4);
        this.addOre(Block.IRON_ORE.id, 70, 2, 4);
        this.addOre(Block.GOLD_ORE.id, 50, 3, 4);
        this.levelLoaderListener.progressStage("Watering..");
        levelGen6 = this;
        long j39 = System.nanoTime();
        long j45 = 0L;
        i46 = Block.WATER.id;
        this.setNextPhase(0);

        for(i14 = 0; i14 < levelGen6.width; ++i14) {
            j45 = j45 + levelGen6.floodFillWithLiquid(i14, levelGen6.depth / 2 - 1, 0, 0, i46) + levelGen6.floodFillWithLiquid(i14, levelGen6.depth / 2 - 1, levelGen6.height - 1, 0, i46);
        }

        for(i14 = 0; i14 < levelGen6.height; ++i14) {
            j45 = j45 + levelGen6.floodFillWithLiquid(0, levelGen6.depth / 2 - 1, i14, 0, i46) + levelGen6.floodFillWithLiquid(levelGen6.width - 1, levelGen6.depth / 2 - 1, i14, 0, i46);
        }

        i14 = levelGen6.width * levelGen6.height / 8000;

        for(i15 = 0; i15 < i14; ++i15) {
            if(i15 % 100 == 0) {
                levelGen6.setNextPhase(i15 * 100 / (i14 - 1));
            }

            i47 = levelGen6.seed.nextInt(levelGen6.width);
            i17 = levelGen6.waterLevel - 1 - levelGen6.seed.nextInt(2);
            i52 = levelGen6.seed.nextInt(levelGen6.height);
            if(levelGen6.blocks[(i17 * levelGen6.height + i52) * levelGen6.width + i47] == 0) {
                j45 += levelGen6.floodFillWithLiquid(i47, i17, i52, 0, i46);
            }
        }

        levelGen6.setNextPhase(100);
        long j51 = System.nanoTime();
        System.out.println("Flood filled " + j45 + " tiles in " + (double)(j51 - j39) / 1000000.0D + " ms");
        this.levelLoaderListener.progressStage("Melting..");
        this.addLava();
        this.levelLoaderListener.progressStage("Growing..");
        this.addBeaches(i11);
        this.levelLoaderListener.progressStage("Planting..");
        this.plantTrees(i11);
        this.addMushrooms(i11);
        World level34;
        (level34 = new World()).surroundingWaterHeight = this.waterLevel;
        level34.assemble(width, 64, height, this.blocks, (byte[])null);
        level34.timeOfCreation = System.currentTimeMillis();
        level34.author = creator;
        level34.name = "A Nice World";
        level34.cloudHeight = depth + 2;
        this.generateTrees(level34, i11);
        this.levelLoaderListener.progressStage("Spawning..");
        i38 = this.width * this.height * this.depth / 800;
        System.out.println(i43 + " mobs");
        return level34;
    }

    private void addBeaches(int[] blocks) {
        int i2 = this.width;
        int i3 = this.height;
        int i4 = this.depth;
        PerlinNoise perlinNoise5 = new PerlinNoise(seed, 8);
        PerlinNoise perlinNoise6 = new PerlinNoise(seed, 8);

        for(int i7 = 0; i7 < i2; ++i7) {
            this.setNextPhase(i7 * 100 / (this.width - 1));

            for(int i8 = 0; i8 < i3; ++i8) {
                boolean z9 = perlinNoise5.getValue((double)i7, (double)i8) > 8.0D;
                boolean z10 = perlinNoise6.getValue((double)i7, (double)i8) > 12.0D;
                int i11;
                int i12 = ((i11 = blocks[i7 + i8 * i2]) * this.height + i8) * this.width + i7;
                int i13;
                if(((i13 = this.blocks[((i11 + 1) * this.height + i8) * this.width + i7] & 255) == Block.WATER.id || i13 == Block.WATER.id) && i11 <= i4 / 2 - 1 && z10) {
                    this.blocks[i12] = (byte)Block.GRAVEL.id;
                }

                if(i13 == 0) {
                    int i14 = Block.GRASS.id;
                    if(i11 <= i4 / 2 - 1 && z9) {
                        i14 = Block.SAND.id;
                    }

                    this.blocks[i12] = (byte)i14;
                }
            }
        }

    }

    private void generateTrees(World level, int[] blocks) {
        int i3 = this.width;
        int i4 = this.width * this.height / 4000;

        for(int i5 = 0; i5 < i4; ++i5) {
            this.setNextPhase(i5 * 50 / (i4 - 1) + 50);
            int i6 = seed.nextInt(this.width);
            int i7 = seed.nextInt(this.height);

            for(int i8 = 0; i8 < 20; ++i8) {
                int i9 = i6;
                int i10 = i7;

                for(int i11 = 0; i11 < 20; ++i11) {
                    i9 += seed.nextInt(6) - seed.nextInt(6);
                    i10 += seed.nextInt(6) - seed.nextInt(6);
                    if(i9 >= 0 && i10 >= 0 && i9 < this.width && i10 < this.height) {
                        int i12 = blocks[i9 + i10 * i3] + 1;
                        if(seed.nextInt(4) == 0) {
                            level.placeTree(i9, i12, i10);
                        }
                    }
                }
            }
        }

    }

    private void plantTrees(int[] blocks) {
        int i2 = this.width;
        int i3 = this.width * this.height / 3000;

        for(int i4 = 0; i4 < i3; ++i4) {
            int i5 = seed.nextInt(2);
            this.setNextPhase(i4 * 50 / (i3 - 1));
            int i6 = seed.nextInt(this.width);
            int i7 = seed.nextInt(this.height);

            for(int i8 = 0; i8 < 10; ++i8) {
                int i9 = i6;
                int i10 = i7;

                for(int i11 = 0; i11 < 5; ++i11) {
                    i9 += seed.nextInt(6) - seed.nextInt(6);
                    i10 += seed.nextInt(6) - seed.nextInt(6);
                    if((i5 < 2 || seed.nextInt(4) == 0) && i9 >= 0 && i10 >= 0 && i9 < this.width && i10 < this.height) {
                        int i12 = blocks[i9 + i10 * i2] + 1;
                        if((this.blocks[(i12 * this.height + i10) * this.width + i9] & 255) == 0) {
                            int i13 = (i12 * this.height + i10) * this.width + i9;
                            if((this.blocks[((i12 - 1) * this.height + i10) * this.width + i9] & 255) == Block.GRASS.id) {
                                if(i5 == 0) {
                                    this.blocks[i13] = (byte)Block.YELLOW_FLOWER.id;
                                } else if(i5 == 1) {
                                    this.blocks[i13] = (byte)Block.RED_FLOWER.id;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void addMushrooms(int[] blocks) {
        int i2 = this.width;
        int i3 = 0;
        int i4 = this.width * this.height * this.depth / 2000;

        for(int i5 = 0; i5 < i4; ++i5) {
            int i6 = seed.nextInt(2);
            this.setNextPhase(i5 * 50 / (i4 - 1) + 50);
            int i7 = seed.nextInt(this.width);
            int i8 = seed.nextInt(this.depth);
            int i9 = seed.nextInt(this.height);

            for(int i10 = 0; i10 < 20; ++i10) {
                int i11 = i7;
                int i12 = i8;
                int i13 = i9;

                for(int i14 = 0; i14 < 5; ++i14) {
                    i11 += seed.nextInt(6) - seed.nextInt(6);
                    i12 += seed.nextInt(2) - seed.nextInt(2);
                    i13 += seed.nextInt(6) - seed.nextInt(6);
                    if((i6 < 2 || seed.nextInt(4) == 0) && i11 >= 0 && i13 >= 0 && i12 >= 1 && i11 < this.width && i13 < this.height && i12 < blocks[i11 + i13 * i2] - 1 && (this.blocks[(i12 * this.height + i13) * this.width + i11] & 255) == 0) {
                        int i15 = (i12 * this.height + i13) * this.width + i11;
                        if((this.blocks[((i12 - 1) * this.height + i13) * this.width + i11] & 255) == Block.STONE.id) {
                            if(i6 == 0) {
                                this.blocks[i15] = (byte)Block.BROWN_MUSHROOM.id;
                            } else if(i6 == 1) {
                                this.blocks[i15] = (byte)Block.RED_MUSHROOM.id;
                            }

                            ++i3;
                        }
                    }
                }
            }
        }

        System.out.println("Added " + i3 + " mushrooms");
    }

    private void addOre(int tile, int rarity, int min, int max) {
        byte b25 = (byte)tile;
        max = this.width;
        int i5 = this.height;
        int i6 = this.depth;
        int i7 = max * i5 * i6 / 256 / 64 * rarity / 100;

        for(int i8 = 0; i8 < i7; ++i8) {
            this.setNextPhase(i8 * 100 / (i7 - 1) / 4 + min * 100 / 4);
            float f9 = seed.nextFloat() * (float)max;
            float f10 = seed.nextFloat() * (float)i6;
            float f11 = seed.nextFloat() * (float)i5;
            int i12 = (int)((seed.nextFloat() + seed.nextFloat()) * 75.0F * (float)rarity / 100.0F);
            float f13 = (float)((double)seed.nextFloat() * Math.PI * 2.0D);
            float f14 = 0.0F;
            float f15 = (float)((double)seed.nextFloat() * Math.PI * 2.0D);
            float f16 = 0.0F;

            for(int i17 = 0; i17 < i12; ++i17) {
                f9 = (float)((double)f9 + Math.sin((double)f13) * Math.cos((double)f15));
                f11 = (float)((double)f11 + Math.cos((double)f13) * Math.cos((double)f15));
                f10 = (float)((double)f10 + Math.sin((double)f15));
                f13 += f14 * 0.2F;
                f14 = (f14 *= 0.9F) + (seed.nextFloat() - seed.nextFloat());
                f15 = (f15 + f16 * 0.5F) * 0.5F;
                f16 = (f16 *= 0.9F) + (seed.nextFloat() - seed.nextFloat());
                float f18 = (float)(Math.sin((double)i17 * Math.PI / (double)i12) * (double)rarity / 100.0D + 1.0D);

                for(int i19 = (int)(f9 - f18); i19 <= (int)(f9 + f18); ++i19) {
                    for(int i20 = (int)(f10 - f18); i20 <= (int)(f10 + f18); ++i20) {
                        for(int i21 = (int)(f11 - f18); i21 <= (int)(f11 + f18); ++i21) {
                            float f22 = (float)i19 - f9;
                            float f23 = (float)i20 - f10;
                            float f24 = (float)i21 - f11;
                            if(f22 * f22 + f23 * f23 * 2.0F + f24 * f24 < f18 * f18 && i19 >= 1 && i20 >= 1 && i21 >= 1 && i19 < this.width - 1 && i20 < this.depth - 1 && i21 < this.height - 1) {
                                int i26 = (i20 * this.height + i21) * this.width + i19;
                                if(this.blocks[i26] == Block.STONE.id) {
                                    this.blocks[i26] = b25;
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    private void setNextPhase(int phase) {
        this.levelLoaderListener.progressPercentage(phase);
    }

    private void addLava() {
        int i1 = 0;
        int i2 = this.width * this.height * this.depth / 20000;

        for(int i3 = 0; i3 < i2; ++i3) {
            if(i3 % 100 == 0) {
                this.setNextPhase(i3 * 100 / (i2 - 1));
            }

            int i4 = seed.nextInt(this.width);
            int i5 = (int)(seed.nextFloat() * seed.nextFloat() * (float)(this.waterLevel - 3));
            int i6 = seed.nextInt(this.height);
            if(this.blocks[(i5 * this.height + i6) * this.width + i4] == 0) {
                ++i1;
                this.floodFillWithLiquid(i4, i5, i6, 0, Block.LAVA.id);
            }
        }

        this.setNextPhase(100);
        System.out.println("LavaCount: " + i1);
    }

    private long floodFillWithLiquid(int x, int y, int z, int source, int tt) {
        byte b20 = (byte)tt;
        ArrayList arrayList21 = new ArrayList();
        byte b6 = 0;
        int i7 = 1;

        int i8;
        for(i8 = 1; 1 << i7 < this.width; ++i7) {
        }

        while(1 << i8 < this.height) {
            ++i8;
        }

        int i9 = this.height - 1;
        int i10 = this.width - 1;
        int i22 = b6 + 1;
        this.coords[0] = ((y << i8) + z << i7) + x;
        long j13 = 0L;
        x = this.width * this.height;

        while(i22 > 0) {
            --i22;
            y = this.coords[i22];
            if(i22 == 0 && arrayList21.size() > 0) {
                System.out.println("IT HAPPENED!");
                this.coords = (int[])arrayList21.remove(arrayList21.size() - 1);
                i22 = this.coords.length;
            }

            z = y >> i7 & i9;
            int i11 = y >> i7 + i8;

            int i12;
            int i15;
            for(i15 = i12 = y & i10; i12 > 0 && this.blocks[y - 1] == 0; --y) {
                --i12;
            }

            while(i15 < this.width && this.blocks[y + i15 - i12] == 0) {
                ++i15;
            }

            int i16 = y >> i7 & i9;
            int i17 = y >> i7 + i8;
            if(i16 != z || i17 != i11) {
                System.out.println("hoooly fuck");
            }

            boolean z23 = false;
            boolean z24 = false;
            boolean z18 = false;
            j13 += (long)(i15 - i12);

            for(i12 = i12; i12 < i15; ++i12) {
                this.blocks[y] = b20;
                boolean z19;
                if(z > 0) {
                    if((z19 = this.blocks[y - this.width] == 0) && !z23) {
                        if(i22 == this.coords.length) {
                            arrayList21.add(this.coords);
                            this.coords = new int[1048576];
                            i22 = 0;
                        }

                        this.coords[i22++] = y - this.width;
                    }

                    z23 = z19;
                }

                if(z < this.height - 1) {
                    if((z19 = this.blocks[y + this.width] == 0) && !z24) {
                        if(i22 == this.coords.length) {
                            arrayList21.add(this.coords);
                            this.coords = new int[1048576];
                            i22 = 0;
                        }

                        this.coords[i22++] = y + this.width;
                    }

                    z24 = z19;
                }

                if(i11 > 0) {
                    byte b25 = this.blocks[y - x];
                    if((b20 == Block.FLOWING_LAVA.id || b20 == Block.LAVA.id) && (b25 == Block.FLOWING_WATER.id || b25 == Block.WATER.id)) {
                        this.blocks[y - x] = (byte)Block.STONE.id;
                    }

                    if((z19 = b25 == 0) && !z18) {
                        if(i22 == this.coords.length) {
                            arrayList21.add(this.coords);
                            this.coords = new int[1048576];
                            i22 = 0;
                        }

                        this.coords[i22++] = y - x;
                    }

                    z18 = z19;
                }

                ++y;
            }
        }

        return j13;
    }
}