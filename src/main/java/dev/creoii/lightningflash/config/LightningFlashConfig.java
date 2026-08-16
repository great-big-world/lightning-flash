package dev.creoii.lightningflash.config;

import dev.creoii.lightningflash.LightningFlash;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.minecraft.util.CommonColors;

@Config(name = "lightningflash")
public class LightningFlashConfig implements ConfigData {
    public int struckByLightningTime = 10;
    public int lightningFlashColor = CommonColors.WHITE;

    public static void init() {
        AutoConfig.register(LightningFlashConfig.class, (conf, clazz) -> new Toml4jConfigSerializer<>(conf, clazz) {
            public LightningFlashConfig deserialize() {
                try {
                    return super.deserialize();
                } catch (Exception e) {
                    return createDefault();
                }
            }
        });

        LightningFlash.CONFIG = AutoConfig.getConfigHolder(LightningFlashConfig.class).getConfig();
    }
}
