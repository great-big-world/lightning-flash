package dev.creoii.lightningflash.config;

import dev.creoii.lightningflash.LightningFlash;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;

public class ModMenuConfig {
    public static Screen createConfigScreen(Screen parent) {
        return createConfigScreenBuilder(parent).build();
    }

    public static ConfigBuilder createConfigScreenBuilder(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Component.translatable("title.lightningflash.config"));
        builder.setSavingRunnable(() -> AutoConfig.getConfigHolder(LightningFlashConfig.class).save());

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory generalCategory = builder.getOrCreateCategory(Component.translatable("lightningflash.config.category.general"));

        generalCategory.addEntry(entryBuilder.startIntSlider(Component.translatable("lightningflash.config.option.struckByLightningTime"), LightningFlash.getStruckByLightningTime(), 0, 100)
                .setDefaultValue(10)
                .setTextGetter(integer -> integer <= 0 ? Component.translatable("lightningflash.config.option.disabled") : Component.translatable("lightningflash.config.option.struckByLightningTime.value", integer))
                .setTooltip(Component.translatable("lightningflash.config.option.struckByLightningTime.tooltip"))
                .setSaveConsumer(LightningFlash::setStruckByLightningTime)
                .build());

        generalCategory.addEntry(entryBuilder.startAlphaColorField(Component.translatable("lightningflash.config.option.lightningFlashColor"), LightningFlash.getLightningFlashColor())
                .setDefaultValue(CommonColors.WHITE)
                .setSaveConsumer(LightningFlash::setLightningFlashColor)
                .build());

        return builder;
    }
}
