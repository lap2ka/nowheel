package dev.lapt.nowheel.mixin;

import com.simibubi.create.content.contraptions.IControlContraption;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntityTicker;
import dev.lapt.nowheel.config.NowheelConfig;
import dev.tr7zw.entityculling.EntityCullingModBase;
import dev.tr7zw.entityculling.versionless.access.Cullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
    value = SmartBlockEntityTicker.class,
    remap = false
)
public abstract class CreateTickCullMixin {

    @Inject(
        method = "tick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void nowheel$cullCulledTick(Level level, BlockPos pos, BlockState state, BlockEntity be, CallbackInfo ci) {
        if (!NowheelConfig.get().tickCulling) return;

        if (!level.isClientSide) return;
        if (EntityCullingModBase.instance.config.skipBlockEntityCulling) return;
        if (be instanceof IControlContraption) return;
        if (!(be instanceof Cullable c)) return;
        if (c.isForcedVisible() || !c.isCulled()) return;
        ci.cancel();
    }
}
