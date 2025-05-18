package com.phantazap.gensettings.mixin;

import net.minecraft.client.render.world.WorldRenderer;
import org.lwjgl.opengl.ContextCapabilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GLContext;getCapabilities()Lorg/lwjgl/opengl/ContextCapabilities;", shift = At.Shift.AFTER), remap = false)
	private boolean isofix$disableOcclusion(ContextCapabilities instance) {
		return false;
	}
}