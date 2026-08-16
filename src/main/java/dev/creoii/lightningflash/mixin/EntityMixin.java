package dev.creoii.lightningflash.mixin;

import dev.creoii.lightningflash.LightningFlash;
import dev.creoii.lightningflash.util.ExtendedEntity;
import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class EntityMixin implements ExtendedEntity {
    @Shadow public abstract Holder<EntityType<?>> typeHolder();
    @Shadow protected UUID uuid;
    @Unique private int gbw$struckByLightningTime;

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueOutput;)V"))    private void gbw$writeExtendedLivingData(ValueOutput view, CallbackInfo ci) {
        view.putShort("StruckByLightningTime", (short) gbw$struckByLightningTime);
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueInput;)V"))    private void gbw$readExtendedLivingData(ValueInput view, CallbackInfo ci) {
        gbw$struckByLightningTime = view.getShortOr("StruckByLightningTime", (short) 0);
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;checkBelowWorld()V"))
    private void gbw$tickExtendedLivingData(CallbackInfo ci) {
        if (gbw$struckByLightningTime > 0) {
            --gbw$struckByLightningTime;
        }
    }

    @Inject(method = "thunderHit", at = @At("TAIL"))
    private void gbw$applyLightningFlash(ServerLevel serverLevel, LightningBolt lightningBolt, CallbackInfo ci) {
        if (!(typeHolder().is(LightningFlash.CANNOT_LIGHTNING_FLASH))) {
            gbw$struckByLightningTime = LightningFlash.getStruckByLightningTime();

            if (gbw$struckByLightningTime > 0) PlayerLookup.tracking((Entity) (Object) this).forEach(serverPlayerEntity -> ServerPlayNetworking.send(serverPlayerEntity, new StruckByLightningS2C(uuid)));
        }
    }

    @Override
    public int gbw$getStruckByLightningTime() {
        return gbw$struckByLightningTime;
    }

    @Override
    public void gbw$setStruckByLightningTime(int struckByLightningTime) {
        gbw$struckByLightningTime = struckByLightningTime;
    }
}
