package net.nukebob.mutema.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.nukebob.mutema.widget.MuteMaButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OptionsScreen.class)
public class OptionsScreenMixin extends Screen {
	@Unique
	private MuteMaButton muteMaButton = new MuteMaButton();

	protected OptionsScreenMixin(Text title) {
		super(title);
	}

	@Inject(at = @At("TAIL"), method = "init")
	private void initMuteMaButton(CallbackInfo info) {
		addDrawableChild(muteMaButton);
	}

	@Inject(at = @At("TAIL"), method = "refreshWidgetPositions")
	private void refreshWidgetPositions(CallbackInfo info) {
		refreshMuteMaButtonPosition();
	}

	@Unique
	private void refreshMuteMaButtonPosition() {
		for (ButtonWidget button : children().stream().filter(element -> element instanceof ButtonWidget).map(e -> (ButtonWidget) e).toList()) {
			if (button.getMessage().equals(Text.translatable("options.sounds"))) {
				int buttonX = button.getX();
				int buttonY = button.getY();
				int buttonWidth = button.getWidth();

				muteMaButton.setPosition(buttonX + buttonWidth + 5, buttonY);

				break;
			}
		}
	}
}