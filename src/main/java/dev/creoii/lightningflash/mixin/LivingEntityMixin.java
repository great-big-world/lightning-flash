package dev.creoii.lightningflash.mixin;

import dev.creoii.lightningflash.util.ExtendedLivingEntity;
import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements ExtendedLivingEntity {
    @Unique
    private int gbw$struckByLightningTime;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "writeCustomData", at = @At("HEAD"))
    private void gbw$writeExtendedLivingData(WriteView view, CallbackInfo ci) {
        view.putShort("StruckByLightningTime", (short) gbw$struckByLightningTime);
    }

    @Inject(method = "readCustomData", at = @At("HEAD"))
    private void gbw$readExtendedLivingData(ReadView view, CallbackInfo ci) {
        gbw$struckByLightningTime = view.getShort("StruckByLightningTime", (short) 0);
    }

    @Inject(method = "baseTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isDead()Z"))
    private void gbw$tickExtendedLivingData(CallbackInfo ci) {
        if (gbw$struckByLightningTime > 0) {
            --gbw$struckByLightningTime;
        }
    }

    @Override
    public void onStruckByLightning(ServerWorld world, LightningEntity lightning) {
        super.onStruckByLightning(world, lightning);
        gbw$struckByLightningTime = 10;

        PlayerLookup.tracking(this).forEach(serverPlayerEntity -> ServerPlayNetworking.send(serverPlayerEntity, new StruckByLightningS2C(getUuid())));
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
