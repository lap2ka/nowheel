package dev.lapt.nowheel.mixin;

import dev.lapt.nowheel.cull.CullTransitions;
import dev.tr7zw.entityculling.versionless.access.Cullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference"})
@Mixin(value = BlockEntity.class, priority = 1100)
public abstract class BlockEntityCullableTrackingMixin {

    @Unique
    private boolean nowheel$transitionPending;
    @Unique
    private boolean nowheel$transitionValue;

    @Inject(method = "setCulled(Z)V", at = @At("HEAD"), remap = false)
    private void nowheel$captureTransition(boolean value, CallbackInfo ci) {
        this.nowheel$transitionPending = ((Cullable) (Object) this).isCulled() != value;
        this.nowheel$transitionValue = value;
    }

    @Inject(method = "setCulled(Z)V", at = @At("TAIL"), remap = false)
    private void nowheel$notifyTransition(boolean value, CallbackInfo ci) {
        if (!this.nowheel$transitionPending || this.nowheel$transitionValue != value) return;
        this.nowheel$transitionPending = false;
        CullTransitions.BE.notify((BlockEntity) (Object) this, value);
    }
}
