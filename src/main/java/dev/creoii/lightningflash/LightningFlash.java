package dev.creoii.lightningflash;

import dev.creoii.lightningflash.config.LightningFlashConfig;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.CommonColors;
import net.minecraft.world.entity.EntityType;

public class LightningFlash implements ModInitializer {
    @Nullable public static LightningFlashConfig CONFIG;
    public static final TagKey<EntityType<?>> CANNOT_LIGHTNING_FLASH = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("great_big_world", "cannot_lightning_flash"));

    @Override
    public void onInitialize() {
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
