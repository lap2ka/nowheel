package dev.lapt.nowheel.util;

import com.simibubi.create.foundation.blockEntity.CachedRenderBBBlockEntity;
import dev.lapt.nowheel.config.NowheelConfig;
import dev.tr7zw.entityculling.EntityCullingModBase;
import dev.tr7zw.entityculling.versionless.EntityCullingVersionlessBase;
import dev.tr7zw.entityculling.versionless.access.Cullable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;

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
        return isCulled((Cullable) blockEntity) || outsideTracingDistance(blockEntity);
    }

    public static boolean isBlockEntityCulledNoDistanceCulling(BlockEntity blockEntity) {
        return isCulled((Cullable) blockEntity);
    }

    public static boolean isCulled(Entity entity) {
        return isCulled((Cullable) entity);
    }

    private static boolean outsideTracingDistance(BlockEntity blockEntity) {
        EntityCullingModBase entityCulling = EntityCullingModBase.instance;
        if (!EntityCullingVersionlessBase.enabled || entityCulling.config.skipBlockEntityCulling || !NowheelConfig.get().distanceCulling) {
            return false;
        }

        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        BlockPos blockEntityPos = blockEntity.getBlockPos();

        double tracingDistanceSqr = entityCulling.config.tracingDistance * entityCulling.config.tracingDistance;
        if (blockEntityPos.distToCenterSqr(cameraPos) <= tracingDistanceSqr) return false;

        if (entityCulling.blockEntityWhitelist.contains(blockEntity.getType()) || entityCulling.isDynamicWhitelisted(blockEntity)) {
            return false;
        }

        if (!(blockEntity instanceof CachedRenderBBBlockEntity crbb)) return true;
        return crbb.getRenderBoundingBox().distanceToSqr(cameraPos) > tracingDistanceSqr;
    }
}
