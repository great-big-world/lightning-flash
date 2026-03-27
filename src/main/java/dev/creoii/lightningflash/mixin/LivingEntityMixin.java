package dev.creoii.lightningflash.mixin;

import dev.creoii.lightningflash.util.ExtendedLivingEntity;
import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ExtendedLivingEntity {
    @Unique
    private int gbw$struckByLightningTime;

    public LivingEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void gbw$writeExtendedLivingData(CompoundTag compoundTag, CallbackInfo ci) {
        compoundTag.putShort("StruckByLightningTime", (short) gbw$struckByLightningTime);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void gbw$readExtendedLivingData(CompoundTag compoundTag, CallbackInfo ci) {
        gbw$struckByLightningTime = compoundTag.getShortOr("StruckByLightningTime", (short) 0);
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;isDeadOrDying()Z"))
    private void gbw$tickExtendedLivingData(CallbackInfo ci) {
        if (gbw$struckByLightningTime > 0) {
            --gbw$struckByLightningTime;
        }
    }

    @Override
    public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt) {
        super.thunderHit(serverLevel, lightningBolt);
        gbw$struckByLightningTime = 10;

        PlayerLookup.tracking(this).forEach(serverPlayerEntity -> ServerPlayNetworking.send(serverPlayerEntity, new StruckByLightningS2C(getUUID())));
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
