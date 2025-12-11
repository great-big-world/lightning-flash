package dev.creoii.lightningflash.client;

import dev.creoii.lightningflash.util.ExtendedLivingEntity;
import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

import java.util.UUID;

public class LightningFlashClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StruckByLightningS2C.PACKET_ID, (struckByLightningS2C, context) -> {
            UUID uuid = struckByLightningS2C.uuid();
            context.client().execute(() -> {
                if (context.client().level.getEntity(uuid) instanceof ExtendedLivingEntity extendedLivingEntity) {
                    extendedLivingEntity.gbw$setStruckByLightningTime(10);
                }
            });
        });
    }
}
