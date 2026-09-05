package dev.lapt.nowheel.core.flywheel;

import dev.engine_room.flywheel.api.backend.Backend;
import dev.engine_room.flywheel.api.backend.BackendManager;
import dev.engine_room.flywheel.api.visual.Visual;
import dev.engine_room.flywheel.api.visualization.VisualManager;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.tr7zw.entityculling.versionless.access.Cullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface CullableVisual {
    static boolean isVisualCulled(Visual visual) {
        return visual instanceof CullableVisual c && c.nowheel$isCulled();
    }

    static boolean isRemovingBackend() {
        ResourceLocation id = Backend.REGISTRY.getId(BackendManager.currentBackend());
        return id == null || !id.getPath().equals("indirect");
    }

    static void changeVisualCullState(BlockEntity blockEntity, boolean culled) {
        if (!isRemovingBackend()) return;
        var level = blockEntity.getLevel();
        if (level == null) return;
        VisualizationManager manager = VisualizationManager.get(level);
        if (manager == null) return;

        toggle(manager.blockEntities(), blockEntity, culled);
    }

    static void changeVisualCullState(Entity entity, boolean culled) {
        if (!isRemovingBackend()) return;
        var level = entity.level();
        VisualizationManager manager = VisualizationManager.get(level);
        if (manager == null) return;

        toggle(manager.entities(), entity, culled);
    }

    private static <T> void toggle(VisualManager<T> visuals, T subject, boolean culled) {
        if (culled && !((Cullable) subject).isForcedVisible()) visuals.queueRemove(subject);
        else visuals.queueAdd(subject);
    }

    boolean nowheel$isCulled();
}
