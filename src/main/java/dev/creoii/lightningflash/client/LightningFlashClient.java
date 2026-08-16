package dev.creoii.lightningflash.client;

import dev.creoii.lightningflash.LightningFlash;
import dev.creoii.lightningflash.util.ExtendedLivingEntity;
import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;

public class LightningFlashClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StruckByLightningS2C.TYPE, (struckByLightningS2C, localPlayer, packetSender) -> {
            if (LightningFlash.getStruckByLightningTime() <= 0) return;

            int id = struckByLightningS2C.id();
            Entity entity = localPlayer.clientLevel.getEntity(id);
            if (entity instanceof ExtendedLivingEntity extendedLivingEntity && !(entity.getType().is(LightningFlash.CANNOT_LIGHTNING_FLASH))) {
                extendedLivingEntity.gbw$setStruckByLightningTime(LightningFlash.getStruckByLightningTime());
            }
        });
    }
}
