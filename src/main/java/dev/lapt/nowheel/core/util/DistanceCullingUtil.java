package dev.lapt.nowheel.core.util;

import com.simibubi.create.foundation.blockEntity.CachedRenderBBBlockEntity;
import dev.lapt.nowheel.config.NowheelConfig;
import dev.lapt.nowheel.core.DistanceCullable;
import dev.tr7zw.entityculling.EntityCullingModBase;
import dev.tr7zw.entityculling.access.Cullable;
import dev.tr7zw.entityculling.versionless.EntityCullingVersionlessBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public final class DistanceCullingUtil {
    private static final int tracingChunkRadius = 8;
    private static ChunkPos currentChunk = null;
    private static AABB currentAABB = null;

    private static volatile boolean isDisabled = true;

    private DistanceCullingUtil() {
    }

    public static boolean isDistanceCulled(BlockEntity blockEntity) {
        return !isDisabled && ((DistanceCullable) blockEntity).nowheel$isDistanceCulled();
    }

    public static void onBeginTick() {
        Minecraft client = Minecraft.getInstance();
        EntityCullingModBase entityCulling = EntityCullingModBase.instance;
        boolean enabled = client.player != null
            && entityCulling != null
            && EntityCullingVersionlessBase.enabled
            && !entityCulling.config.skipBlockEntityCulling
            && NowheelConfig.get().distanceCulling;

        if (enabled) {
            isDisabled = false;
            ChunkPos chunk = client.player.chunkPosition();
            if (chunk.equals(currentChunk)) return;

            currentChunk = chunk;
            // I understand nothing
            currentAABB = new AABB(
                (chunk.x - tracingChunkRadius) << 4, Double.NEGATIVE_INFINITY, (chunk.z - tracingChunkRadius) << 4,
                (chunk.x + tracingChunkRadius + 1) << 4, Double.POSITIVE_INFINITY, (chunk.z + tracingChunkRadius + 1) << 4
            );
        }
        else {
            if (isDisabled) return;
            isDisabled = true;
            currentChunk = null;
        }

        updateAll(client);
    }

    private static void updateAll(Minecraft client) {
        var level = client.level;
        if (level == null || client.player == null) return;

        ChunkPos center = client.player.chunkPosition();
        int radius = Math.max(tracingChunkRadius, client.options.getEffectiveRenderDistance()) + 1;

        for (int x = center.x - radius; x <= center.x + radius; x++) {
            for (int z = center.z - radius; z <= center.z + radius; z++) {
                level.getChunk(x, z).getBlockEntities().values().forEach(DistanceCullingUtil::update);
            }
        }
    }

    public static void update(BlockEntity blockEntity) {
        ((DistanceCullable) blockEntity).nowheel$setDistanceCulled(!isDisabled && outsideTracingDistance(blockEntity));
    }

    private static boolean outsideTracingDistance(BlockEntity blockEntity) {
        if (currentChunk == null) return false;

        BlockPos blockEntityPos = blockEntity.getBlockPos();
        int dx = Math.abs(SectionPos.blockToSectionCoord(blockEntityPos.getX()) - currentChunk.x);
        int dz = Math.abs(SectionPos.blockToSectionCoord(blockEntityPos.getZ()) - currentChunk.z);
        if (dx <= tracingChunkRadius && dz <= tracingChunkRadius) return false;

        EntityCullingModBase entityCulling = EntityCullingModBase.instance;
        if (entityCulling.blockEntityWhitelist.contains(blockEntity.getType()) || entityCulling.isBlockEntityDynamicWhitelisted((Cullable) blockEntity)) {
            return false;
        }

        if (!(blockEntity instanceof CachedRenderBBBlockEntity crbb)) return true;
        return !crbb.getRenderBoundingBox().intersects(currentAABB);
    }
}
