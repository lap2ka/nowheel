package dev.lapt.nowheel.mixin.flywheel;

import dev.engine_room.flywheel.lib.visual.AbstractEntityVisual;
import dev.lapt.nowheel.core.flywheel.CullableVisual;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import static dev.lapt.nowheel.core.util.IsCulledUtil.isCulled;

@Mixin(
    value = AbstractEntityVisual.class,
    remap = false
)
public abstract class AbstractEntityVisualMixin implements CullableVisual {

    @Shadow
    @Final
    protected Entity entity;

    @Override
    public boolean nowheel$isCulled() {
        return isCulled(entity);
    }
}
