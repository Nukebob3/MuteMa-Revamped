package net.nukebob.mutema.mixin;

import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.client.sound.TickableSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.nukebob.mutema.MuteMa;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public abstract class SoundManagerMixin {
    @Shadow @Final private SoundSystem soundSystem;

    @Shadow public abstract void setVolume(SoundInstance sound, float volume);

    @Inject(method = "play*", at=@At("HEAD"))
    private void cancelSound(SoundInstance sound, CallbackInfo ci) {
        if (MuteMa.MUTED) {
            setVolume(sound, 0);
        }
    }

    @Inject(method = "playNextTick", at=@At("HEAD"))
    private void cancelNextTickSound(TickableSoundInstance sound, CallbackInfo ci) {
        if (MuteMa.MUTED) {
            setVolume(sound, 0);
        }
    }

    @Inject(method = "updateSoundVolume", at = @At("HEAD"), cancellable = true)
    private void updateSoundVolume(SoundCategory category, float volume, CallbackInfo ci) {
        if (MuteMa.MUTED) {
            volume = 0;
            this.soundSystem.updateSoundVolume(category, volume);

            ci.cancel();
        }
    }
}
