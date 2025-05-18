package com.phantazap.gensettings.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.vertex.Tesselator;
import org.lwjgl.opengl.GL11;

public class CGBackgroundRenderer {
    public static void drawBackgroundTexture(Minecraft minecraft, int width, int height) {
        GL11.glDisable(2896);
        GL11.glDisable(2912);
        Tesselator var2 = Tesselator.INSTANCE;
        GL11.glBindTexture(3553, minecraft.textureManager.load("/assets/gensettings/mossy_cobble.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        var2.begin();
        var2.color(4210752);
        var2.vertex(0.0F, (float)height, 0.0F, 0.0F, (float)height / 32.0F);
        var2.vertex((float)width, (float)height, 0.0F, (float)width / 32.0F, (float)height / 32.0F);
        var2.vertex((float)width, 0.0F, 0.0F, (float)width / 32.0F, 0.0F);
        var2.vertex(0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        var2.end();
    }
}
