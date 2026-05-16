package dev.lapt.nowheel;

import com.mojang.logging.LogUtils;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.lapt.nowheel.cull.CullTransitions;
import dev.lapt.nowheel.flywheel.FlywheelVisualToggleListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(NowheelMod.MODID)
public class NowheelMod {
    public static final String MODID = "nowheel";
    public static final Logger LOGGER = LogUtils.getLogger();

    public NowheelMod() {
        if (FMLEnvironment.dist != Dist.CLIENT) return;
        CullTransitions.BE.register(new FlywheelVisualToggleListener<>(
            BlockEntity::getLevel, VisualizationManager::blockEntities));
        CullTransitions.ENTITY.register(new FlywheelVisualToggleListener<>(
            Entity::level, VisualizationManager::entities));
        LOGGER.info("Nowheel loaded");
    }
}
