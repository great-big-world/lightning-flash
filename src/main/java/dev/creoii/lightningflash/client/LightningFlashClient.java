package dev.creoii.lightningflash.client;

import dev.creoii.lightningflash.util.ExtendedLivingEntity;
import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class LightningFlashClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(StruckByLightningS2C.TYPE, (struckByLightningS2C, localPlayer, packetSender) -> {
            int id = struckByLightningS2C.id();
            if (localPlayer.clientLevel.getEntity(id) instanceof ExtendedLivingEntity extendedLivingEntity) {
                extendedLivingEntity.gbw$setStruckByLightningTime(10);
            }
        });
    }
}
