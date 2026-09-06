package dev.lapt.nowheel.mixin.flywheel.legacy_removing;

import dev.engine_room.flywheel.api.visual.EntityVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.impl.visualization.storage.EntityStorage;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static dev.lapt.nowheel.core.util.CullingUtil.isRemovingBackend;
import static dev.lapt.nowheel.core.util.IsCulledUtil.isCulled;

// just to be safe ig
@SuppressWarnings("AmbiguousMixinReference")
@Mixin(
    value = EntityStorage.class,
    remap = false
)
public abstract class EntityStorageMixin {

    @Inject(
        method = "willAccept",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$rejectCulledAccept(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (isRemovingBackend() && isCulled(entity)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
        method = "createRaw",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$rejectCulledCreate(
        VisualizationContext context,
        Entity obj,
        float partialTick,
        CallbackInfoReturnable<EntityVisual<?>> cir
    )
    {
        if (isRemovingBackend() && isCulled(obj)) {
            cir.setReturnValue(null);
        }
    }
}