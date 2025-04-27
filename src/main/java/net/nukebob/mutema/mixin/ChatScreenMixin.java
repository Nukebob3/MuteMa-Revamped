package net.nukebob.mutema.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.nukebob.mutema.MuteMa;
import net.nukebob.mutema.widget.MuteMaButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {
    @Unique
    private MuteMaButton muteMaButton = new MuteMaButton();

    @Unique
    ArrayList<ButtonWidget> widgets = new ArrayList<>();

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at= @At("TAIL"))
    private void addMuteMaButton(CallbackInfo ci) {
        widgets.add(muteMaButton);
        refreshMuteMaButtonPosition();
    }

    @Inject(method = "render", at= @At("TAIL"))
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        widgets.forEach(btn -> {
            if (btn instanceof MuteMaButton && !MuteMa.MUTED) return;
            btn.render(context, mouseX, mouseY, delta);
        });
    }

    @Unique
    private void refreshMuteMaButtonPosition() {
        muteMaButton.setPosition(width-45, 40);
    }

    @Inject(method = "mouseClicked", at=@At("HEAD"), cancellable = true)
    private void buttonInteract(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        for (ButtonWidget widget : widgets) {
            if (widget instanceof MuteMaButton && !MuteMa.MUTED) continue;

            if (widget.isHovered()||widget.isFocused()) {
                widget.onPress();
                widget.playDownSound(client.getSoundManager());
                cir.setReturnValue(true);
                return;
            }
        }
    }
}
