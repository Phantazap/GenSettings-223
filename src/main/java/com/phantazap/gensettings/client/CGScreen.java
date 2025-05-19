package com.phantazap.gensettings.client;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;

import java.util.Random;

public class CGScreen extends Screen {
    private Screen parent;
    public int selectedType;
    public int selectedTheme;
    public int worldWidth;
    public int worldLength;
    public int worldHeight;
    public Random seed = new Random(2048);
    public int surroundingWaterHeight;
    public int surroundingGroundHeight;

    public CGScreen (Screen parent, int types, int shapes, int sizes, int themes) {
        this.parent = parent;
        selectedType = types;
        selectedTheme = themes;

        //Get default world size values from main generation screen.
        worldLength = worldWidth = 128 << sizes;
        worldHeight = 64;
        if (shapes == 1) {
            worldWidth /= 2;
            worldLength <<= 1;
        } else if (shapes == 2) {
            worldLength = worldWidth /= 2;
            worldHeight = 256;
        }

        //Get default water and ground heights from main generation screen.


        //Forcibly set values to be used
        ///*
            worldWidth = 128;
            worldLength = 128;
            worldHeight = 8192;
         //*/
    }

    public final void init() {
        this.buttons.clear();
        this.buttons.add(new ButtonWidget(0, this.width / 2 - 100, this.height / 4, "Sussy"));
        this.buttons.add(new ButtonWidget(1, this.width / 2 - 100, this.height / 4 + 24, "Baka"));
        this.buttons.add(new ButtonWidget(2, this.width / 2 - 100, this.height / 4 + 48, "Flower"));
        this.buttons.add(new ButtonWidget(1000, this.width / 2 - 100, this.height / 4 + 120 + 12, "Cancel"));
    }

    protected final void buttonClicked(ButtonWidget button) {
        if (button.id == 1000) {
            this.minecraft.openScreen(this.parent);
        } else if (button.id == 0) {
            CGCreateWorld.createWorld(this.minecraft, this.worldLength, this.worldWidth, this.worldHeight, this.selectedType, this.selectedTheme, this.seed, this.surroundingWaterHeight, this.surroundingGroundHeight);
            this.minecraft.openScreen(null);
        }  else if (button.id == 1) {
            worldWidth = 256;
            worldLength = 256;
            worldHeight = 64;
            ClassicCreateWorld.createWorld(this.minecraft, this.worldLength, this.worldWidth, this.worldHeight, this.seed);
            this.minecraft.openScreen(null);
        }   else if (button.id == 2) {
            worldWidth = 256;
            worldLength = 256;
            worldHeight = 64;
            FlowerCreateWorld.createWorld(this.minecraft, this.worldLength, this.worldWidth, this.worldHeight);
            this.minecraft.openScreen(null);
        }
    }

    public final void render(int mouseX, int mouseY, float tickDelta) {
        CGBackgroundRenderer.drawBackgroundTexture(minecraft, width, height);
        super.render(mouseX, mouseY, tickDelta);
    }
}
