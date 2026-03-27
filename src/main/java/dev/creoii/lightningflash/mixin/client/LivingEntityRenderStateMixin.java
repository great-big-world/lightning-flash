package dev.creoii.lightningflash.mixin.client;

import dev.creoii.lightningflash.util.ExtendedLivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements ExtendedLivingEntityRenderState {
    @Unique
    private boolean gbw$struckByLightning;

    @Override
    public boolean gbw$isStruckByLightning() {
        return gbw$struckByLightning;
    }

    @Override
    public void gbw$setStruckByLightning(boolean struckByLightning) {
        gbw$struckByLightning = struckByLightning;
    }
}
