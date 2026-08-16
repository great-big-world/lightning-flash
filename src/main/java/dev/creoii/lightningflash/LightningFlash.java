package dev.creoii.lightningflash;

import dev.creoii.lightningflash.config.LightningFlashConfig;
import dev.creoii.lightningflash.util.StruckByLightningS2C;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.EntityType;
import org.jspecify.annotations.Nullable;

public class LightningFlash implements ModInitializer {
    @Nullable public static LightningFlashConfig CONFIG;
    public static final TagKey<EntityType<?>> CANNOT_LIGHTNING_FLASH = TagKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath("great_big_world", "cannot_lightning_flash"));

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playS2C().register(StruckByLightningS2C.PACKET_ID, StruckByLightningS2C.PACKET_CODEC);

        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            LightningFlashConfig.init();
        }
    }

    public static int getStruckByLightningTime() {
        return CONFIG == null ? 10 : CONFIG.struckByLightningTime;
    }

    public static void setStruckByLightningTime(int struckByLightningTime) {
        if (CONFIG != null) CONFIG.struckByLightningTime = struckByLightningTime;
    }

    public static int getLightningFlashColor() {
        return CONFIG == null ? CommonColors.WHITE : CONFIG.lightningFlashColor;
    }

    public static void setLightningFlashColor(int lightningFlashColor) {
        if (CONFIG != null) CONFIG.lightningFlashColor = lightningFlashColor;
    }
}
