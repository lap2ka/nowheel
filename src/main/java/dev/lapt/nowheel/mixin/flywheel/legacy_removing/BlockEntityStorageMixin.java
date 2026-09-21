package dev.lapt.nowheel.mixin.flywheel.legacy_removing;

import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.impl.visualization.storage.BlockEntityStorage;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.lapt.nowheel.core.util.LegacyCullingUtil.isRemovingBackend;
import static dev.lapt.nowheel.core.util.IsCulledUtil.isBlockEntityCulledNoDistanceCulling;

// just to be safe ig
@SuppressWarnings("AmbiguousMixinReference")
@Mixin(
    value = BlockEntityStorage.class,
    remap = false
)
public abstract class BlockEntityStorageMixin {

    @Inject(
        method = "willAccept",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$rejectCulledAccept(BlockEntity blockEntity, CallbackInfoReturnable<Boolean> cir) {
        if (isRemovingBackend() && isBlockEntityCulledNoDistanceCulling(blockEntity)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
        method = "createRaw",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$rejectCulledCreate(
        VisualizationContext visualizationContext,
        BlockEntity obj,
        float partialTick,
        CallbackInfoReturnable<BlockEntityVisual<?>> cir
    )
    {
        if (isRemovingBackend() && isBlockEntityCulledNoDistanceCulling(obj)) {
            cir.setReturnValue(null);
        }
    }
}