package dev.lapt.nowheel.mixin.entity_culling.legacy_removing;

import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.lapt.nowheel.core.util.LegacyCullingUtil.changeVisualCullState;

@SuppressWarnings({"MixinAnnotationTarget", "UnresolvedMixinReference"})
@Mixin(
    value = BlockEntity.class,
    priority = 1100
)
public abstract class BlockEntityMixin {
    @Inject(
        method = "setCulled(Z)V",
        at = @At("TAIL"),
        remap = false
    )
    private void nowheel$notifyTransition(boolean value, CallbackInfo ci) {
        changeVisualCullState((BlockEntity) (Object) this);
    }
}
