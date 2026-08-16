package dev.creoii.lightningflash.client;

import dev.creoii.lightningflash.LightningFlash;
import dev.creoii.lightningflash.util.ExtendedEntity;
import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.world.entity.Entity;

import java.util.UUID;

public class LightningFlashClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StruckByLightningS2C.PACKET_ID, (struckByLightningS2C, context) -> {
            if (LightningFlash.getStruckByLightningTime() <= 0) return;

            UUID uuid = struckByLightningS2C.uuid();
            context.client().execute(() -> {
                Entity entity = context.client().level.getEntity(uuid);
                if (entity instanceof ExtendedEntity extendedEntity && !(entity.getType().is(LightningFlash.CANNOT_LIGHTNING_FLASH))) {
                    extendedEntity.gbw$setStruckByLightningTime(LightningFlash.getStruckByLightningTime());
                }
            });
        });
    }
}
