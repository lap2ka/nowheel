package dev.lapt.nowheel.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.tr7zw.entityculling.EntityCullingModBase;
import dev.tr7zw.entityculling.versionless.access.Cullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Lots of Create stuff marks itself as shouldRenderOffScreen which EntityCulling happily
// respects. Cull those anyway. We make AABBs in CreateBoxes for the
// stuff that breaks
@Mixin(value = BlockEntityRenderDispatcher.class, priority = 500)
public abstract class BlockEntityRenderDispatcherOcclusionCullMixin {

    @Inject(
        method = "render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$cullEvenWhenOffScreen(BlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buf, CallbackInfo ci)
    {
        if (EntityCullingModBase.instance.config.skipBlockEntityCulling) return;
        if (!(be instanceof Cullable c)) return;
        if (c.isForcedVisible() || !c.isCulled()) return;
        EntityCullingModBase.instance.skippedBlockEntities++;
        ci.cancel();
    }
}
