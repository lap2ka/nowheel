package dev.lapt.nowheel.mixin;

import dev.engine_room.flywheel.lib.visualization.VisualizationHelper;
import dev.tr7zw.entityculling.CullTask;
import dev.tr7zw.entityculling.EntityCullingModBase;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = CullTask.class, remap = false)
public abstract class CullTaskMixin {

    @Unique
    private static final BlockEntityRenderer<BlockEntity> nowheel$FLYWHEEL_VISUAL_RENDERER = (blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay) -> {};

    // in my testing some flywheel stuff (simple kinetic, etc) didn't appear to have a renderer,
    // nor did they subsequently get culled, so we give them one
    @SuppressWarnings({"rawtypes", "MixinAnnotationTarget", "InvalidInjectorMethodSignature"})
    @Redirect(
        method = "cullBlockEntities", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderDispatcher;getRenderer(Lnet/minecraft/world/level/block/entity/BlockEntity;)Lnet/minecraft/client/renderer/blockentity/BlockEntityRenderer;",
        remap = true
    )
    )
    private BlockEntityRenderer nowheel$includeFlywheelOnlyVisuals(BlockEntityRenderDispatcher dispatcher, BlockEntity blockEntity) {
        BlockEntityRenderer renderer = dispatcher.getRenderer(blockEntity);
        if (renderer != null) return renderer;
        if (VisualizationHelper.canVisualize(blockEntity)) return nowheel$FLYWHEEL_VISUAL_RENDERER;
        return null;
    }

    // Flywheel renders its stuff way past the vanilla 64
    @Redirect(
        method = "cullBlockEntities", at = @At(
        value = "INVOKE", target = "Ldev/tr7zw/entityculling/CullTask;closerThan"
    )
    )
    private boolean nowheel$extendCloserThan(BlockPos blockPos, Position position, double original) {
        double d = EntityCullingModBase.instance.config.tracingDistance;
        double dx = (blockPos.getX() + 0.5) - position.x();
        double dy = (blockPos.getY() + 0.5) - position.y();
        double dz = (blockPos.getZ() + 0.5) - position.z();
        return dx * dx + dy * dy + dz * dz < d * d;
    }
}
