package dev.lapt.nowheel.mixin;

import dev.engine_room.flywheel.api.visual.EntityVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.impl.visualization.storage.EntityStorage;
import dev.tr7zw.entityculling.versionless.access.Cullable;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// just to be safe ig
@Mixin(value = EntityStorage.class, remap = false)
public abstract class EntityStorageMixin {

    @Inject(
        method = "willAccept", at = @At("HEAD"), cancellable = true
    )
    private void nowheel$rejectCulledAdd(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) entity instanceof Cullable c && !c.isForcedVisible() && c.isCulled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(
        method = "createRaw",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$skipCulledCreate(VisualizationContext context, Entity obj, float partialTick, CallbackInfoReturnable<EntityVisual<?>> cir)
    {
        if ((Object) obj instanceof Cullable c && !c.isForcedVisible() && c.isCulled()) {
            cir.setReturnValue(null);
        }
    }
}
