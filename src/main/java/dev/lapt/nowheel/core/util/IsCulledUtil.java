package dev.lapt.nowheel.core.util;

import dev.tr7zw.entityculling.access.Cullable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class IsCulledUtil {
    private IsCulledUtil() {
    }

    public static boolean isCulled(Object obj) {
        return obj instanceof Cullable c && isCulled(c);
    }

    public static boolean isCulled(Cullable c) {
        return c.isCulled() && !c.isForcedVisible();
    }

    public static boolean isCulled(BlockEntity blockEntity) {
        return isCulled((Cullable) blockEntity) || DistanceCullingUtil.isDistanceCulled(blockEntity);
    }

    public static boolean isBlockEntityCulledNoDistanceCulling(BlockEntity blockEntity) {
        return isCulled((Cullable) blockEntity);
    }

    public static boolean isCulled(Entity entity) {
        return isCulled((Cullable) entity);
    }
}
