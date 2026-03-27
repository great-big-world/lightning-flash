package dev.creoii.lightningflash;

import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class LightningFlash implements ModInitializer {
    @Override
    public void onInitialize() {
        PayloadTypeRegistry.clientboundPlay().register(StruckByLightningS2C.PACKET_ID, StruckByLightningS2C.PACKET_CODEC);
    }
}
