package dev.lapt.nowheel.mixin;

import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.impl.visualization.storage.BlockEntityStorage;
import dev.tr7zw.entityculling.versionless.access.Cullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// just to be safe ig
@Mixin(value = BlockEntityStorage.class, remap = false)
public abstract class BlockEntityStorageMixin {

    @Inject(
        method = "willAccept", at = @At("HEAD"), cancellable = true
    )
    private void nowheel$rejectCulledAdd(BlockEntity blockEntity, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) blockEntity instanceof Cullable c && !c.isForcedVisible() && c.isCulled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
        method = "createRaw",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$skipCulledCreate(
        VisualizationContext visualizationContext,
        BlockEntity obj,
        float partialTick,
        CallbackInfoReturnable<BlockEntityVisual<?>> cir
    )
    {
        if ((Object) obj instanceof Cullable c && !c.isForcedVisible() && c.isCulled()) {
            cir.setReturnValue(null);
        }
    }
}
