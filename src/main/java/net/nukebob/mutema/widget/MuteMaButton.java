package net.nukebob.mutema.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.nukebob.mutema.MuteMa;

public class MuteMaButton extends ButtonWidget {protected MuteMaButton(int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }

    public MuteMaButton() {
        this(0, 0, 20, 20, Text.empty(), MuteMaButton::pressed, (textSupplier) -> Text.translatable(MuteMa.MUTED?"mutema.muted":"mutema.unmuted"));
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);
        int i = this.getX() + this.getWidth() - 16 - 2;
        int j = this.getY() + this.getHeight() / 2 - 16 / 2;
        Identifier texture = Identifier.of(MuteMa.MOD_ID, "icon/"+(MuteMa.MUTED?"muted":"unmuted"));
        context.drawGuiTexture(RenderLayer::getGuiTextured, texture, i, j, 16, 16);
    }

    protected static void pressed(ButtonWidget button) {
        MinecraftClient client = MinecraftClient.getInstance();
        MuteMa.MUTED=!MuteMa.MUTED;
        if (!MuteMa.MUTED) {
            for (SoundCategory category : SoundCategory.values()) {
                client.getSoundManager().updateSoundVolume(category, client.options.getSoundVolume(category));
            }
        } else {
            for (SoundCategory category : SoundCategory.values()) {
                client.getSoundManager().updateSoundVolume(category, 0);
            }
        }
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        if (!MuteMa.MUTED) {
            soundManager.play(PositionedSoundInstance.master(SoundEvents.ENTITY_GOAT_SCREAMING_AMBIENT, 1.0F, 5.0f));
        }
    }
}
