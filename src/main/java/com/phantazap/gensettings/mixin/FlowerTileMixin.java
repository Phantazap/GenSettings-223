package com.phantazap.gensettings.mixin;

import net.minecraft.block.PlantBlock;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PlantBlock.class)
public class FlowerTileMixin {
	@Inject(method = "canSurvive", at = @At(value = "HEAD"), cancellable = true)
	private void genSettings$canSurvive(World x, int y, int z, int par4, CallbackInfoReturnable<Boolean> cir) {
		// Flowers won't pop off any more.
		cir.setReturnValue(true);
	}
}