package dev.lapt.nowheel.mixin.entity_culling.legacy_removing;

import dev.tr7zw.entityculling.access.Cullable;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.lapt.nowheel.core.util.LegacyCullingUtil.changeVisualCullState;

@SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference"})
@Mixin(
    value = Entity.class,
    priority = 1100
)
public abstract class EntityMixin {
    @Unique
    private boolean nowheel$transitionPending;
    @Unique
    private boolean nowheel$transitionValue;

    @Inject(
        method = "setCulled(Z)V",
        at = @At("HEAD"),
        remap = false
    )
    private void nowheel$captureTransition(boolean value, CallbackInfo ci) {
        this.nowheel$transitionPending = ((Cullable) this).isCulled() != value;
        this.nowheel$transitionValue = value;
    }

    @Inject(
        method = "setCulled(Z)V",
        at = @At("TAIL"),
        remap = false
    )
    private void nowheel$notifyTransition(boolean value, CallbackInfo ci) {
        if (!this.nowheel$transitionPending || this.nowheel$transitionValue != value) return;
        this.nowheel$transitionPending = false;
        changeVisualCullState((Entity) (Object) this, value);
    }
}
