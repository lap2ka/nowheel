package dev.lapt.nowheel;

import com.mojang.logging.LogUtils;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.lapt.nowheel.compat.SableCompat;
import dev.lapt.nowheel.cull.CullTransitions;
import dev.lapt.nowheel.flywheel.FlywheelVisualToggleListener;
import dev.tr7zw.entityculling.EntityCullingModBase;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(value = NowheelMod.MODID, dist = net.neoforged.api.distmarker.Dist.CLIENT)
public class NowheelMod {
    public static final String MODID = "nowheel";
    public static final Logger LOGGER = LogUtils.getLogger();

    private boolean sableHooked = false;

    public NowheelMod() {
        CullTransitions.BE.register(new FlywheelVisualToggleListener<>(
            BlockEntity::getLevel, VisualizationManager::blockEntities));
        CullTransitions.ENTITY.register(new FlywheelVisualToggleListener<>(
            Entity::level, VisualizationManager::entities));

        if (ModList.get().isLoaded("sablecompanion")) {
            NeoForge.EVENT_BUS.addListener(this::hookSable);
        }

        LOGGER.info("Nowheel loaded");
    }

    // https://github.com/tr7zw/EntityCulling/issues/299
    // I could PR but idk
    private void hookSable(ClientTickEvent.Post event) {
        if (sableHooked) {
            return;
        }
        EntityCullingModBase ec = EntityCullingModBase.instance;
        if (ec == null) {
            return;
        }
        sableHooked = true;
        NeoForge.EVENT_BUS.unregister(this);
        ec.addDynamicEntityWhitelist(SableCompat::onSubLevel);
        ec.addDynamicBlockEntityWhitelist(SableCompat::onSubLevel);
        LOGGER.info("Nowheel Sable compat active");
    }
}
