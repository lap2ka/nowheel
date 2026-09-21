package dev.lapt.nowheel.mixin;

import dev.lapt.nowheel.core.DistanceCullable;
import dev.lapt.nowheel.core.util.DistanceCullingUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements DistanceCullable {

    @Unique
    private volatile boolean nowheel$distanceCulled;

    @Override
    public boolean nowheel$isDistanceCulled() {
        return nowheel$distanceCulled;
    }

    @Override
    public void nowheel$setDistanceCulled(boolean culled) {
        if (nowheel$distanceCulled != culled) nowheel$distanceCulled = culled;
    }

    @Inject(
        method = "setLevel",
        at = @At("TAIL")
    )
    private void nowheel$initDistanceCulling(Level level, CallbackInfo ci) {
        if (level.isClientSide) DistanceCullingUtil.update((BlockEntity) (Object) this);
    }
}
