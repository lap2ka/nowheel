package dev.lapt.nowheel;

import com.mojang.logging.LogUtils;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.lapt.nowheel.cull.CullTransitions;
import dev.lapt.nowheel.flywheel.FlywheelVisualToggleListener;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.slf4j.Logger;

public class NowheelMod implements ClientModInitializer {
    public static final String MODID = "nowheel";
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("Convert2MethodRef")
    @Override
    public void onInitializeClient() {
        CullTransitions.BE.register(new FlywheelVisualToggleListener<>(be -> be.getLevel(), VisualizationManager::blockEntities));
        CullTransitions.ENTITY.register(new FlywheelVisualToggleListener<>(e -> e.level(), VisualizationManager::entities));
        LOGGER.info("Nowheel loaded");
    }
}
