package dev.lapt.nowheel.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class NowheelConfigScreen {

    private NowheelConfigScreen() { }

    public static Screen create(Screen parent) {
        NowheelConfig config = NowheelConfig.get();

        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("text.nowheel.title"))
            .setSavingRunnable(config::write);

        ConfigCategory general = builder.getOrCreateCategory(Component.translatable("text.nowheel.category.general"));
        ConfigEntryBuilder entry = builder.entryBuilder();

        general.addEntry(
            entry.startBooleanToggle(Component.translatable("text.nowheel.overrideEntityCulling"), config.overrideEntityCulling)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("text.nowheel.overrideEntityCulling.tooltip"))
                .setSaveConsumer(v -> config.overrideEntityCulling = v)
                .build()
        );

        general.addEntry(
            entry.startBooleanToggle(Component.translatable("text.nowheel.tickCulling"), config.tickCulling)
                .setDefaultValue(true)
                .setTooltip(Component.translatable("text.nowheel.tickCulling.tooltip"))
                .setSaveConsumer(v -> config.tickCulling = v)
                .build()
        );

        general.addEntry(
            entry.startBooleanToggle(Component.translatable("text.nowheel.distanceCulling"), config.distanceCulling)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("text.nowheel.distanceCulling.tooltip"))
                .setSaveConsumer(v -> config.distanceCulling = v)
                .build()
        );

        general.addEntry(
            entry.startBooleanToggle(Component.translatable("text.nowheel.forceRemoving"), config.forceRemoving)
                .setDefaultValue(false)
                .setTooltip(Component.translatable("text.nowheel.forceRemoving.tooltip"))
                .setSaveConsumer(v -> {
                    if (v != config.forceRemoving) {
                        config.forceRemoving = v;
                        Minecraft.getInstance().levelRenderer.allChanged();
                    }
                })
                .build()
        );

        return builder.build();
    }
}
