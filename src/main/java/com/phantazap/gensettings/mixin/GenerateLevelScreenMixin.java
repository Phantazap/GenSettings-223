package com.phantazap.gensettings.mixin;

import com.phantazap.gensettings.client.CGButton;
import com.phantazap.gensettings.client.CGScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.CreateWorldScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(CreateWorldScreen.class)
public class GenerateLevelScreenMixin extends Screen {

	@Shadow private String[] types;

	@Shadow private int selectedType;

	@Shadow private int selectedShape;

	@Shadow private int selectedSize;

	@Shadow private int selectedTheme;

	@Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/world/CreateWorldScreen;addButtons()V"))
	private void addCGButton(CallbackInfo ci) {
		this.buttons.add(new CGButton(6, 16, 16, 20, 20, "CG"));
	}

	@Inject(method = "buttonClicked", at = @At(value = "TAIL"))
	private void clickCGButton(ButtonWidget button, CallbackInfo ci) {
		if (button.id == 6) {
			this.minecraft.openScreen((Screen) new CGScreen(this.minecraft.screen, selectedType, selectedShape, selectedSize, selectedTheme));
		}
	}
}