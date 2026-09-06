package dev.lapt.nowheel.core.util;

import com.simibubi.create.foundation.blockEntity.CachedRenderBBBlockEntity;
import dev.lapt.nowheel.config.NowheelConfig;
import dev.tr7zw.entityculling.EntityCullingModBase;
import dev.tr7zw.entityculling.versionless.EntityCullingVersionlessBase;
import dev.tr7zw.entityculling.access.Cullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

public final class IsCulledUtil {
    private static final double tracingDistanceSqr = 128 * 128;

    private static boolean isDistanceCullingEnabled;
    private static Vec3 cameraPos = Vec3.ZERO;

    private IsCulledUtil() {
    }

    public static boolean isCulled(Object obj) {
        return obj instanceof Cullable c && isCulled(c);
    }

    public static boolean isCulled(Cullable c) {
        return c.isCulled() && !c.isForcedVisible();
    }

    public static boolean isCulled(BlockEntity blockEntity) {
        return isCulled((Cullable) blockEntity) || outsideTracingDistance(blockEntity);
    }

    public static boolean isBlockEntityCulledNoDistanceCulling(BlockEntity blockEntity) {
        return isCulled((Cullable) blockEntity);
    }

    public static boolean isCulled(Entity entity) {
        return isCulled((Cullable) entity);
    }

    public static void onBeginTick() {
        EntityCullingModBase entityCulling = EntityCullingModBase.instance;
        isDistanceCullingEnabled = entityCulling != null
            && EntityCullingVersionlessBase.enabled
            && !entityCulling.config.skipBlockEntityCulling
            && NowheelConfig.get().distanceCulling;

        if (isDistanceCullingEnabled) cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
    }

    private static boolean outsideTracingDistance(BlockEntity blockEntity) {
        if (!isDistanceCullingEnabled) return false;

        BlockPos blockEntityPos = blockEntity.getBlockPos();
        if (blockEntityPos.distToCenterSqr(cameraPos.x, cameraPos.y, cameraPos.z) <= tracingDistanceSqr) return false;

        EntityCullingModBase entityCulling = EntityCullingModBase.instance;
        if (entityCulling.blockEntityWhitelist.contains(blockEntity.getType()) || entityCulling.isBlockEntityDynamicWhitelisted((Cullable) blockEntity)) {
            return false;
        }

        if (!(blockEntity instanceof CachedRenderBBBlockEntity crbb)) return true;
        return crbb.getRenderBoundingBox().distanceToSqr(cameraPos) > tracingDistanceSqr;
    }
}
