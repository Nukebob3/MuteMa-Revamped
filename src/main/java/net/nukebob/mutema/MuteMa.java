package net.nukebob.mutema;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MuteMa implements ClientModInitializer {
	public static final String MOD_ID = "mutema";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static boolean MUTED = false;

	@Override
	public void onInitializeClient() {
		LOGGER.info("MuteMa Revamped loaded");

		HudLayerRegistrationCallback.EVENT.register(layeredDrawerWrapper -> {
			layeredDrawerWrapper.addLayer(new IdentifiedLayer() {
				@Override
				public Identifier id() {
					return Identifier.of(MOD_ID, "muted");
				}

				@Override
				public void render(DrawContext context, RenderTickCounter tickCounter) {
					if (MUTED) {
						RenderSystem.enableBlend();
						RenderSystem.defaultBlendFunc();
						RenderSystem.setShaderColor(1, 1, 1, 0.5f);

						Text text = Text.translatable("mutema.game_muted");
						int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(text);

						context.getMatrices().push();
						context.getMatrices().translate(context.getScaledWindowWidth()-textWidth-25, 25, 0);
						context.drawText(MinecraftClient.getInstance().textRenderer, text, 0, 0, 5635925, true);
						context.fill(-2,-2,textWidth+2,MinecraftClient.getInstance().textRenderer.fontHeight+2, 0x80000000);
						context.getMatrices().pop();

						RenderSystem.disableBlend();
						RenderSystem.setShaderColor(1, 1, 1, 1f);
					}
				}
			});
		});
	}
}