package dev.lapt.nowheel.core.util;

import dev.engine_room.flywheel.api.backend.Backend;
import dev.engine_room.flywheel.api.backend.BackendManager;
import dev.engine_room.flywheel.api.visualization.VisualManager;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.lapt.nowheel.config.NowheelConfig;
import dev.tr7zw.entityculling.access.Cullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

import static dev.lapt.nowheel.core.util.IsCulledUtil.isCulled;

public final class LegacyCullingUtil {
    private LegacyCullingUtil() {
    }

    public static boolean isRemovingBackend() {
        if (NowheelConfig.get().forceRemoving) return true;
        ResourceLocation id = Backend.REGISTRY.getId(BackendManager.currentBackend());
        return id == null || !id.getPath().equals("indirect");
    }

    public static void changeVisualCullState(BlockEntity blockEntity) {
        if (!isRemovingBackend()) return;
        var level = blockEntity.getLevel();
        if (level == null) return;
        VisualizationManager manager = VisualizationManager.get(level);
        if (manager == null) return;

        VisualManager<BlockEntity> visuals = manager.blockEntities();
        if (isCulled(blockEntity)) visuals.queueRemove(blockEntity);
        else visuals.queueAdd(blockEntity);
    }

    public static void changeVisualCullState(Entity entity, boolean culled) {
        if (!isRemovingBackend()) return;
        var level = entity.level();
        VisualizationManager manager = VisualizationManager.get(level);
        if (manager == null) return;

        VisualManager<Entity> visuals = manager.entities();
        if (culled && !((Cullable) entity).isForcedVisible()) visuals.queueRemove(entity);
        else visuals.queueAdd(entity);
    }
}
