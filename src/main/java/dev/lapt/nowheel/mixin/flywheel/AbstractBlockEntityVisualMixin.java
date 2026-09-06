package dev.lapt.nowheel.mixin.flywheel;

import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.lapt.nowheel.core.flywheel.CullableVisual;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static dev.lapt.nowheel.core.util.IsCulledUtil.isCulled;

@Mixin(
    value = AbstractBlockEntityVisual.class,
    remap = false
)
public abstract class AbstractBlockEntityVisualMixin implements CullableVisual {

    @Shadow
    @Final
    protected BlockEntity blockEntity;

    @Override
    public boolean nowheel$isCulled() {
        return isCulled(blockEntity);
    }
}
