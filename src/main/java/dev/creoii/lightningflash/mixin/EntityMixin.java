package dev.creoii.lightningflash.mixin;

import dev.creoii.lightningflash.LightningFlash;
import dev.creoii.lightningflash.util.ExtendedLivingEntity;
import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin implements ExtendedLivingEntity {
    @Shadow @Final private EntityType<?> type;
    @Shadow private int id;
    @Unique private int gbw$struckByLightningTime;

    @Inject(method = "saveWithoutId", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void gbw$writeExtendedLivingData(CompoundTag compoundTag, CallbackInfoReturnable<CompoundTag> cir) {
        compoundTag.putShort("StruckByLightningTime", (short) gbw$struckByLightningTime);
    }

    @Inject(method = "load", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V"))
    private void gbw$readExtendedLivingData(CompoundTag compoundTag, CallbackInfo ci) {
        gbw$struckByLightningTime = compoundTag.getShort("StruckByLightningTime");
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;checkBelowWorld()V"))
    private void gbw$tickExtendedLivingData(CallbackInfo ci) {
        if (!(type.is(LightningFlash.CANNOT_LIGHTNING_FLASH)) && gbw$struckByLightningTime > 0) {
            --gbw$struckByLightningTime;
        }
    }

    @Inject(method = "thunderHit", at = @At("TAIL"))
    private void gbw$applyLightningFlash(ServerLevel serverLevel, LightningBolt lightningBolt, CallbackInfo ci) {
        if (!(type.is(LightningFlash.CANNOT_LIGHTNING_FLASH))) {
            gbw$struckByLightningTime = LightningFlash.getStruckByLightningTime();

            if (gbw$struckByLightningTime > 0) PlayerLookup.tracking((Entity) (Object) this).forEach(serverPlayerEntity -> ServerPlayNetworking.send(serverPlayerEntity, new StruckByLightningS2C(id)));
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
