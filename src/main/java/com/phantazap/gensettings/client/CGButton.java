package com.phantazap.gensettings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.TextRenderer;
import org.lwjgl.opengl.GL11;

public class CGButton extends ButtonWidget {

    public int x;
    public int y;
    public int width;
    public int height;

    public CGButton(int id, int x, int y, int width, int height, String message) {
        super(id, x, y, width, height, message);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void render(Minecraft minecraft, int mouseX, int mouseY) {
        if (this.visible) {
            TextRenderer var4 = minecraft.textRenderer;
            GL11.glBindTexture(3553, minecraft.textureManager.load("/assets/gensettings/cgbutton.png"));
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            int texture = 1;
            boolean hover = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (!this.active) {
                texture = 0;
            } else if (hover) {
                texture = 2;
            }

            this.drawTexture(this.x, this.y, 0, texture * 20, this.width, this.height);
            if (!this.active) {
                drawCenteredString(var4, this.message, this.x + this.width / 2, this.y + (this.height - 8) / 2, 0x5F5F60);
            } else if (hover) {
                drawCenteredString(var4, this.message, this.x + this.width / 2, this.y + (this.height - 8) / 2, 0xD680D1);
            } else {
                drawCenteredString(var4, this.message, this.x + this.width / 2, this.y + (this.height - 8) / 2, 0x5F1875);
            }
        }
    }
}
